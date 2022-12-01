package me.wietlol.konfig.core.datasources

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Path

data class FlatDataSource(
	val data: Map<Path, Any?>
) : DataSource
{
	override fun resolve(path: Path): Any? =
		data[path]
	
	override fun allValues(): Sequence<Pair<Path, Any?>> =
		data.entries
			.asSequence()
			.map { Pair(it.key, it.value) }
	
	companion object Factory
	{
		fun from(dataSource: DataSource): FlatDataSource =
			FlatDataSource(dataSource.allValues().toMap())
	}
}
