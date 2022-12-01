package me.wietlol.konfig.core.valueconverters

import me.wietlol.konfig.api.SimpleValueConverter
import me.wietlol.konfig.api.UnsupportedTypeConversionException
import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.common.EventId
import me.wietlol.loggo.common.ScopedSourceLogger
import me.wietlol.loggo.common.logWarning

class BooleanConverter(
	logger: CommonLogger
) : SimpleValueConverter<Boolean>
{
	private val unsupportedBooleanConversionEventId = EventId(2113987768, "unsupported-boolean-conversion")
	private val logger = ScopedSourceLogger(logger) { it + "BooleanConverter" }
	
	override fun convert(value: Any?): Boolean? =
		when (value)
		{
			null -> null
			is Boolean -> value
			is Short -> value != 0.toShort()
			is Int -> value != 0
			is Long -> value != 0L
			is Float -> value != 0f
			is Double -> value != 0.0
			is String -> value.toBoolean()
			else -> {
				logger.logWarning(unsupportedBooleanConversionEventId, mapOf(
					"value" to value
				))
				throw UnsupportedTypeConversionException(value, Boolean::class.java)
			}
		}
}
