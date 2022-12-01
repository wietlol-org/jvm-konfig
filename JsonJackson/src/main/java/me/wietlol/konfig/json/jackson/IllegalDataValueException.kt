package me.wietlol.konfig.json.jackson

class IllegalDataValueException(
	message: String,
	val typeName: String,
	cause: Throwable? = null
) : Exception(message, cause)
