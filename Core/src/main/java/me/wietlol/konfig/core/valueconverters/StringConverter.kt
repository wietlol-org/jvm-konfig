package me.wietlol.konfig.core.valueconverters

import me.wietlol.konfig.api.SimpleValueConverter
import me.wietlol.konfig.api.UnsupportedTypeConversionException
import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.common.EventId
import me.wietlol.loggo.common.ScopedSourceLogger
import me.wietlol.loggo.common.logWarning

class StringConverter(
	logger: CommonLogger
) : SimpleValueConverter<String>
{
	private val unsupportedStringConversionEventId = EventId(656959037, "unsupported-string-conversion")
	private val logger = ScopedSourceLogger(logger) { it + "StringConverter" }
	
	override fun convert(value: Any?): String? =
		when (value)
		{
			null -> null
			is Boolean -> value.toString()
			is Short -> value.toString()
			is Int -> value.toString()
			is Long -> value.toString()
			is Float -> value.toString()
			is Double -> value.toString()
			is String -> value
			else -> {
				logger.logWarning(unsupportedStringConversionEventId, mapOf(
					"value" to value
				))
				throw UnsupportedTypeConversionException(value, String::class.java)
			}
		}
}
