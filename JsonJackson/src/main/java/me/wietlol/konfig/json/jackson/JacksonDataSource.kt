package me.wietlol.konfig.json.jackson

import com.fasterxml.jackson.databind.JsonNode
import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.PathParser
import me.wietlol.konfig.core.datasources.FlatDataSource

class JacksonDataSource private constructor(
	val internalDataSource: DataSource
) : DataSource by internalDataSource
{
	companion object Factory
	{
		operator fun invoke(rootNode: JsonNode, pathParser: PathParser): JacksonDataSource
		{
			val values = loadValues(rootNode)
				.map { Pair(pathParser.parse(it.first), it.second) }
				.toMap()
			
			return JacksonDataSource(
				FlatDataSource(values)
			)
		}
		
		private fun loadValues(node: JsonNode): Sequence<Pair<String, Any?>> =
			loadValues(node, "")
		
		private fun loadValues(node: JsonNode, prefix: String): Sequence<Pair<String, Any?>> =
			if (node.isObject)
				node.fields()
					.asSequence()
					.flatMap { loadValues(it.value, "$prefix.${it.key}") }
			else
				sequenceOf(Pair(prefix, node.getValue()))
		
		private fun JsonNode.getValue(): Any? =
			when
			{
				isNull -> null
				isTextual -> textValue()
				isBinary -> binaryValue()
				isBoolean -> booleanValue()
				// integrals
				isShort -> shortValue()
				isInt -> intValue()
				isLong -> longValue()
				isBigInteger -> bigIntegerValue()
				// decimals
				isFloat -> floatValue()
				isDouble -> doubleValue()
				isBigDecimal -> decimalValue()
				else -> throw IllegalDataValueException("Node had illegal type for Konfig DataSource value.", nodeType.name)
			}
	}
}
