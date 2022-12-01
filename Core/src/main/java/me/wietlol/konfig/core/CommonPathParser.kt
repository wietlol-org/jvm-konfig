package me.wietlol.konfig.core

import me.wietlol.konfig.api.Path
import me.wietlol.konfig.api.PathParser

data class CommonPathParser(
	val delimiter: String // at the moment, only dots work as delimiters
) : PathParser
{
	override fun parse(path: String): Path =
		path
			.splitToSequence(delimiter)
			.filter { it.isNotEmpty() }
			.toList()
			.let { CommonPath(it) }
}
