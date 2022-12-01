package me.wietlol.konfig.core

import me.wietlol.konfig.api.Konfig
import me.wietlol.konfig.api.Path
import kotlin.reflect.KClass

class SubKonfig(
	val root: Konfig,
	val prefix: Path
) : Konfig by root
{
	override fun <T> get(path: Path, type: KClass<*>): T =
		root.get(prefix + path, type)
	
	override fun getSection(path: Path): Konfig =
		root.getSection(prefix + path)
}
