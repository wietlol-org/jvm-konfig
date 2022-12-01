package me.wietlol.konfig.core

import org.junit.Test

class CommonKonfigBuilderTests : LocalTestModule()
{
	@Test
	fun `assert that a konfig builder fails if it is missing a path parser and value resolver`() = unitTest {
		val builder = CommonKonfigBuilder()
		
		assertThrows<UninitializedPropertyAccessException> { builder.build() }
	}
}
