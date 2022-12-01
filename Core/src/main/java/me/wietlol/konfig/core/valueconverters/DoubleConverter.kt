package me.wietlol.konfig.core.valueconverters

import me.wietlol.konfig.api.SimpleValueConverter
import me.wietlol.konfig.api.UnsupportedTypeConversionException
import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.common.EventId
import me.wietlol.loggo.common.ScopedSourceLogger
import me.wietlol.loggo.common.logWarning

class DoubleConverter(
	logger: CommonLogger
) : SimpleValueConverter<Double>
{
	private val unsupportedDoubleConversionEventId = EventId(953805084, "unsupported-double-conversion")
	private val logger = ScopedSourceLogger(logger) { it + "DoubleConverter" }
	
	override fun convert(value: Any?): Double? =
		when (value)
		{
			null -> null
			is Boolean -> if (value == true) 1.0 else 0.0
			is Short -> value.toDouble()
			is Int -> value.toDouble()
			is Long -> value.toDouble()
			is Float -> value.toDouble()
			is Double -> value
			is String -> value.toDouble()
			else -> {
				logger.logWarning(unsupportedDoubleConversionEventId, mapOf(
					"value" to value
				))
				throw UnsupportedTypeConversionException(value, Double::class.java)
			}
		}
}
