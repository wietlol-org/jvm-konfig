package me.wietlol.konfig.json.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Path
import me.wietlol.konfig.core.CommonPath
import me.wietlol.konfig.core.CommonPathParser
import org.junit.Test

class JacksonDataSourceTests : LocalTestModule()
{
	@Test
	fun `assert that an empty json object results in no values`() = unitTest {
		val json = "{}"
		val pathParser = CommonPathParser(".")
		
		val node: JsonNode = ObjectMapper().readTree(json)
		
		val dataSource: DataSource = JacksonDataSource(node, pathParser)
		
		val values: Map<Path, Any?> = dataSource.allValues().toMap()
		
		val expected: Map<Path, Any?> = mapOf()
		assertThat(values)
			.isEqualTo(expected)
	}
	
	@Test
	fun `assert that string properties result in values`() = unitTest {
		val json = """{"foo":"bar"}"""
		val pathParser = CommonPathParser(".")
		
		val node: JsonNode = ObjectMapper().readTree(json)
		
		val dataSource: DataSource = JacksonDataSource(node, pathParser)
		
		val values: Map<Path, Any?> = dataSource.allValues().toMap()
		
		val expected: Map<Path, Any?> = mapOf(
			CommonPath(listOf("foo")) to "bar"
		)
		assertThat(values)
			.isEqualTo(expected)
	}
	
	@Test
	fun `assert that nested objects and dot keys result in nested values`() = unitTest {
		val json = """
			{
			    "foo": "bar",
			    "baz": {
			        "burp": 4,
			        "derp.lerp": 5,
			        "derp.terp": null
			    }
			}
		""".trimIndent()
		val pathParser = CommonPathParser(".")
		
		val node: JsonNode = ObjectMapper().readTree(json)
		
		val dataSource: DataSource = JacksonDataSource(node, pathParser)
		
		val values: Map<Path, Any?> = dataSource.allValues().toMap()
		
		val expected: Map<Path, Any?> = mapOf(
			CommonPath(listOf("foo")) to "bar",
			CommonPath(listOf("baz", "burp")) to 4,
			CommonPath(listOf("baz", "derp", "lerp")) to 5,
			CommonPath(listOf("baz", "derp", "terp")) to null
		)
		assertThat(values)
			.isEqualTo(expected)
	}
}
