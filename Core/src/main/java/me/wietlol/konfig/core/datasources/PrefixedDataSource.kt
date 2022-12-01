package me.wietlol.konfig.core.datasources

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Path
import me.wietlol.konfig.core.CommonPath

data class PrefixedDataSource(
	val dataSource: DataSource,
	val prefix: Path
) : DataSource
{
	override fun resolve(path: Path): Any? =
		if (path.startsWith(prefix))
			dataSource.resolve(path.offset(prefix))
		else
			null
	
	private fun Path.startsWith(prefix: Path): Boolean =
		nodes.size > prefix.nodes.size
			&& nodes
			.zip(prefix.nodes)
			.all { it.first == it.second }
	
	// assuming that Path.startsWith(prefix)
	private fun Path.offset(prefix: Path): Path =
		CommonPath(nodes.drop(prefix.nodes.size))
	
	override fun allValues(): Sequence<Pair<Path, Any?>> =
		dataSource
			.allValues()
			.map { Pair(prefix + it.first, it.second) }
}
