package me.wietlol.konfig.core

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.InstantiationFailedException
import me.wietlol.konfig.api.KonfigName
import me.wietlol.konfig.api.MissingKonfigNameAnnotationException
import me.wietlol.konfig.api.MissingRequiredValueException
import me.wietlol.konfig.api.Path
import me.wietlol.konfig.api.ValueResolver
import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.common.EventId
import me.wietlol.loggo.common.ScopedSourceLogger
import me.wietlol.loggo.common.logTrace
import me.wietlol.loggo.core.loggers.NoOpLogger
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import kotlin.NullPointerException
import kotlin.reflect.KClass

class CommonValueResolver(
	val simpleValueResolver: SimpleValueResolver,
	logger: CommonLogger = NoOpLogger(),
) : ValueResolver
{
	private val resolveValueEventId = EventId(1485015697, "resolve-value")
	private val logger = ScopedSourceLogger(logger) { it + "CommonValueResolver" }
	
	@Suppress("UNCHECKED_CAST")
	override fun <T> resolve(path: Path, dataSource: DataSource, type: KClass<*>): T?
	{
		logger.logTrace(resolveValueEventId, mapOf(
			"path" to path,
			"type" to type.java
		))
		
		val simpleValue = simpleValueResolver.resolve<T>(path, dataSource, type)
		if (simpleValue != null)
			return simpleValue.value
		
		class Result<T>(val value: T?, val exception: Throwable?)
		
		val errors = mutableListOf<Throwable>()
		return (type
			.java
			.constructors
			.asSequence()
			.map {
				try
				{
					Result(it.createInstance(path, dataSource), null)
				}
				catch (ex: Exception)
				{
					Result(null, ex)
				}
			}
			.onEach {
				if (it.exception != null)
					errors.add(it.exception!!)
			}
			.dropWhile { it.exception != null }
			.firstOrNull()
			?: throw InstantiationFailedException("Cannot construct instance of type '${type}'.", errors.first(), errors))
			.value
			as T?
	}
	
	private fun <T> Constructor<T>.createInstance(prefix: Path, dataSource: DataSource): T?
	{
		// according to https://stackoverflow.com/a/2729595/2764866
		// we have to rely on annotations to consistently get the name of the parameters
		val arguments = parameters
			.map {
				val konfigName = it.getAnnotation(KonfigName::class.java)
					?: throw MissingKonfigNameAnnotationException(this, it.name)
				
				val fullPath = prefix + (konfigName.name)
				val x = String::class.isValue
				resolve<Any?>(fullPath, dataSource, it.type.kotlin)
			}
			.toTypedArray()
		
		try
		{
			return this.newInstance(*arguments)
		}
		catch (ex: InvocationTargetException)
		{
			when (val cause = ex.cause)
			{
				is IllegalArgumentException -> recoverInformation(prefix, cause)
				is NullPointerException -> recoverInformation(prefix, cause)
				else -> throw ex
			}
		}
		catch (ex: IllegalArgumentException)
		{
			recoverInformation(prefix, ex)
		}
	}
	
	private fun recoverInformation(prefix: Path, ex: Exception): Nothing
	{
		if (ex.message?.startsWith("Parameter specified as non-null is null:") == true)
		{
			val parameterName = ".*, parameter (.+)$".toRegex()
				.matchEntire(ex.message!!)!!
				.groups[1]!!
				.value
			throw MissingRequiredValueException("Missing required value in configuration.", ex, prefix + parameterName)
		}
		else
			throw ex
	}
}
