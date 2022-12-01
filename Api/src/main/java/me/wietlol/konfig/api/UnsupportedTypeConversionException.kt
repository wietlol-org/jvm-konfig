package me.wietlol.konfig.api

class UnsupportedTypeConversionException(
	val value: Any,
	val target: Class<*>,
	cause: Throwable? = null
) : Exception("Unsupported conversion from '${value.javaClass.name}' to '${target.name}'.", cause)
