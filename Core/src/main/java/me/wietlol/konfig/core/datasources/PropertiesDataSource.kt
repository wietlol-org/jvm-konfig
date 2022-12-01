package me.wietlol.konfig.core.datasources

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.PathParser
import java.util.*

class PropertiesDataSource private constructor(
	val internalDataSource: DataSource
) : DataSource by internalDataSource
{
	companion object Factory
	{
		operator fun invoke(properties: Properties, pathParser: PathParser): PropertiesDataSource
		{
			val values = properties
				.entries
				.filter { it.key is String }
				.map { Pair(pathParser.parse(it.key as String), it.value) }
				.toMap()
			
			return PropertiesDataSource(
				FlatDataSource(values)
			)
		}
	}
}
