package me.wietlol.konfig.api

import java.lang.reflect.Constructor

class MissingKonfigNameAnnotationException(
	val constructor: Constructor<*>,
	val parameterName: String
) : Exception("Missing konfig name annotation for parameter '$parameterName' for constructor '$constructor'")
