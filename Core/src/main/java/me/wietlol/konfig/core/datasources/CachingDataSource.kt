package me.wietlol.konfig.core.datasources

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Path

class CachingDataSource(
	val internalDataSource: DataSource
) : DataSource by internalDataSource
{
	private val cacheMap: MutableMap<Path, Any?> = HashMap()
	
	override fun resolve(path: Path): Any? =
		cacheMap.computeIfAbsent(path) {
			internalDataSource.resolve(path)
		}
}
