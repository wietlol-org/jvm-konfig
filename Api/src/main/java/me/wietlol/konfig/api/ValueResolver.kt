package me.wietlol.konfig.api

import kotlin.reflect.KClass

interface ValueResolver
{
	fun <T> resolve(path: Path, dataSource: DataSource, type: KClass<*>): T?
}
