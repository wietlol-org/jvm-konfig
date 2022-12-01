package me.wietlol.konfig.json.jackson

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import me.wietlol.unittest.core.models.TestModule
import me.wietlol.unittest.core.models.TestOptions

open class LocalTestModule : TestModule
{
	override val options: TestOptions = TestOptions(
		mapper = jsonMapper {
			addModule(kotlinModule())
			addModule(ParameterNamesModule())
			addModule(Jdk8Module())
			addModule(JavaTimeModule())
		}
	)
}
