package me.wietlol.konfig.core.valueconverters

import me.wietlol.konfig.api.SimpleValueConverter
import me.wietlol.konfig.api.UnsupportedTypeConversionException
import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.common.EventId
import me.wietlol.loggo.common.ScopedSourceLogger
import me.wietlol.loggo.common.logWarning

class IntConverter(
	logger: CommonLogger
) : SimpleValueConverter<Int>
{
	private val unsupportedIntConversionEventId = EventId(1019911954, "unsupported-int-conversion")
	private val logger = ScopedSourceLogger(logger) { it + "IntConverter" }
	
	override fun convert(value: Any?): Int? =
		when (value)
		{
			null -> null
			is Boolean -> if (value == true) 1 else 0
			is Short -> value.toInt()
			is Int -> value
			is Long -> value.toInt()
			is Float -> value.toInt()
			is Double -> value.toInt()
			is String -> value.toInt()
			else -> {
				logger.logWarning(unsupportedIntConversionEventId, mapOf(
					"value" to value
				))
				throw UnsupportedTypeConversionException(value, Int::class.java)
			}
		}
}
