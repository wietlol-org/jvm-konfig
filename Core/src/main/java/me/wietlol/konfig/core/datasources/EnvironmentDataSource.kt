package me.wietlol.konfig.core.datasources

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Path
import me.wietlol.konfig.api.PathParser

class EnvironmentDataSource private constructor(
	val internalDataSource: DataSource
) : DataSource by internalDataSource
{
	companion object Factory
	{
		operator fun invoke(pathParser: PathParser): EnvironmentDataSource
		{
			val values = System
				.getenv()
				.map { Pair(map(pathParser, it.key), it.value) }
				.toMap()
			
			return EnvironmentDataSource(
				FlatDataSource(values)
			)
		}
		
		private fun map(pathParser: PathParser, name: String): Path =
			pathParser.parse(name.replace("_", "."))
	}
}
