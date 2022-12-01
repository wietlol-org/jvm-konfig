package me.wietlol.konfig.api

import kotlin.reflect.KClass

interface Konfig : PathParser
{
	fun <T> get(path: Path, type: KClass<*>): T
	
	fun getSection(path: Path): Konfig
	
	fun getSection(path: String): Konfig =
		getSection(parse(path))
}
