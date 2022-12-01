package me.wietlol.konfig.core

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Konfig
import me.wietlol.konfig.api.Path
import me.wietlol.konfig.api.PathParser
import me.wietlol.konfig.api.ValueResolver
import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.common.EventId
import me.wietlol.loggo.common.ScopedSourceLogger
import me.wietlol.loggo.common.logTrace
import kotlin.reflect.KClass

class RootKonfig(
	val dataSource: DataSource,
	val valueResolver: ValueResolver,
	val pathParser: PathParser,
	logger: CommonLogger
) : Konfig, PathParser by pathParser
{
	private val loadValueEventId = EventId(926538660, "load-value")
	private val loadSectionEventId = EventId(105329346, "load-section")
	private val logger = ScopedSourceLogger(logger) { it + "RootKonfig" }
	
	@Suppress("UNCHECKED_CAST", "RemoveExplicitTypeArguments")
	override fun <T> get(path: Path, type: KClass<*>): T
	{
		logger.logTrace(loadValueEventId, mapOf(
			"path" to path,
			"type" to type.java
		))
		return valueResolver.resolve<T>(path, dataSource, type) as T
	}
	
	override fun getSection(path: Path): Konfig
	{
		logger.logTrace(loadSectionEventId, mapOf(
			"path" to path
		))
		return SubKonfig(this, path)
	}
}
