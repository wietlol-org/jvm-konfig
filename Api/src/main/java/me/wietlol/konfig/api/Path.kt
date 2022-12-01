package me.wietlol.konfig.api

interface Path
{
	val nodes: List<String>
	
	operator fun plus(tail: Path): Path
	
	operator fun plus(child: String): Path
	
	fun toFlatString(): String =
		nodes.joinToString(".")
}
