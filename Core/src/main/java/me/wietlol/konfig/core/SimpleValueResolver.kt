package me.wietlol.konfig.core

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Path
import kotlin.reflect.KClass

interface SimpleValueResolver
{
	data class SimpleValue<T>(val value: T?)
	
	fun <T> resolve(path: Path, dataSource: DataSource, type: KClass<*>): SimpleValue<T>?
}
