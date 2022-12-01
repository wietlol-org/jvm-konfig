package me.wietlol.konfig.core

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Path
import me.wietlol.konfig.api.SimpleValueConverter
import me.wietlol.konfig.core.SimpleValueResolver.SimpleValue
import me.wietlol.konfig.core.valueconverters.BooleanConverter
import me.wietlol.konfig.core.valueconverters.DoubleConverter
import me.wietlol.konfig.core.valueconverters.IntConverter
import me.wietlol.konfig.core.valueconverters.StringConverter
import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.common.EventId
import me.wietlol.loggo.common.ScopedSourceLogger
import me.wietlol.loggo.common.logTrace
import me.wietlol.loggo.core.loggers.NoOpLogger
import kotlin.reflect.KClass

class CommonSimpleValueResolver(
	logger: CommonLogger = NoOpLogger()
) : SimpleValueResolver
{
	private val valueLoadEventId = EventId(1047343428, "value-load")
	private val logger = ScopedSourceLogger(logger) { it + "CommonSimpleValueResolver" }
	
	private val booleanConverter = BooleanConverter(this.logger)
	private val intConverter = IntConverter(this.logger)
	private val doubleConverter = DoubleConverter(this.logger)
	private val stringConverter = StringConverter(this.logger)
	
	override fun <T> resolve(path: Path, dataSource: DataSource, type: KClass<*>): SimpleValue<T>?
	{
		@Suppress("UNCHECKED_CAST")
		val converter = getSimpleConverter(type) as SimpleValueConverter<T>? ?: return null
		
		val value = dataSource.resolve(path) ?: return SimpleValue(null)
		
		logger.logTrace(valueLoadEventId, mapOf(
			"path" to path,
			"type" to type.java,
			"value" to value
		))
		
		return SimpleValue(converter.convert(value))
	}
	
	private fun getSimpleConverter(type: KClass<*>): SimpleValueConverter<*>? =
		when (type)
		{
			Boolean::class -> booleanConverter
			java.lang.Boolean::class -> booleanConverter
			Int::class -> intConverter
			java.lang.Integer::class -> intConverter
			Double::class -> doubleConverter
			java.lang.Double::class -> doubleConverter
			String::class -> stringConverter
			java.lang.String::class -> stringConverter
			else -> null
		}
}
