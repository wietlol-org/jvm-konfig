package me.wietlol.konfig.core

import me.wietlol.konfig.api.Path

data class CommonPath(
	override val nodes: List<String>
) : Path
{
	override fun plus(tail: Path): Path =
		CommonPath(nodes + tail.nodes)
	
	override fun plus(child: String): Path =
		CommonPath(nodes + child)
}
