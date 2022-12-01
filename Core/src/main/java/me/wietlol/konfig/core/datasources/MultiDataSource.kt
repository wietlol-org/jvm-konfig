package me.wietlol.konfig.core.datasources

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Path

data class MultiDataSource(
	val dataSources: List<DataSource>
) : DataSource
{
	override fun resolve(path: Path): Any? =
		dataSources
			.asSequence()
			.map { it.resolve(path) }
			.filterNotNull()
			.firstOrNull()
	
	override fun allValues(): Sequence<Pair<Path, Any?>> =
		dataSources
			.asSequence()
			.flatMap { it.allValues() }
			.distinct()
}
