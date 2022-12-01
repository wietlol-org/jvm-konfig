package me.wietlol.konfig.core

import org.junit.Test

class CommonPathParserTests : LocalTestModule()
{
	@Test
	fun `assert that PathParser splits on the delimiter`() = unitTest {
		val parser = CommonPathParser(".")
		val pathText = "me.wietlol.konfig"
		
		val path = parser.parse(pathText)
		
		assertThat(path.nodes)
			.isEqualTo(listOf("me", "wietlol", "konfig"))
	}
	
	@Test
	fun `assert that PathParser yields an empty path on an empty string`() = unitTest {
		val parser = CommonPathParser(".")
		val pathText = ""
		
		val path = parser.parse(pathText)
		
		assertThat(path.nodes)
			.assert("empty") { it.isEmpty() }
	}
}
