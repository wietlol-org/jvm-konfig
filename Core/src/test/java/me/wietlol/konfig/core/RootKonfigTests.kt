package me.wietlol.konfig.core

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Konfig
import me.wietlol.konfig.api.Path
import me.wietlol.konfig.api.PathParser
import me.wietlol.konfig.api.ValueResolver
import me.wietlol.konfig.api.get
import me.wietlol.loggo.core.loggers.NoOpLogger
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class RootKonfigTests : LocalTestModule()
{
	private fun createKonfig(dataSource: DataSource, valueResolver: ValueResolver, pathParser: PathParser): Konfig =
		RootKonfig(dataSource, valueResolver, pathParser, NoOpLogger())
	
	@Test
	fun `assert that RootKonfig reads information from the DataSource`() = unitTest {
		val dataSource = mock<DataSource> { }
		val valueResolver = mock<ValueResolver> {
			on { resolve<Any?>(any(), any(), any()) } doReturn "Hello!"
		}
		val pathParser = CommonPathParser(".")
		
		val konfig: Konfig = createKonfig(dataSource, valueResolver, pathParser)
		
		val value = konfig.get<String>("")
		
		assertThat(value)
			.isEqualTo("Hello!")
	}
	
	@Test
	fun `assert that a RootKonfig section reads information from the DataSource`() = unitTest {
		val path = mock<Path> {
			on { nodes } doReturn emptyList()
			on { plus(any<String>()) } doReturn mock
			on { plus(any<Path>()) } doReturn mock
		}
		val dataSource = mock<DataSource> { }
		val valueResolver = mock<ValueResolver> {
			on { resolve<Any?>(any(), any(), any()) } doReturn "Hello!"
		}
		val pathParser = mock<PathParser> { }
		
		val konfig: Konfig = createKonfig(dataSource, valueResolver, pathParser)
			.getSection(path)
		
		val value = konfig.get<String>(path, String::class)
		
		assertThat(value)
			.isEqualTo("Hello!")
	}
	
	@Test
	fun `assert that a RootKonfig section reads information from the DataSource on a deeper path`() = unitTest {
		val type = String::class
		val dataSource = mock<DataSource> { }
		val valueResolver = mock<ValueResolver> {
			on { resolve<Any?>(CommonPath(listOf("foo", "bar")), dataSource, type) } doReturn "Hello!"
		}
		val pathParser = mock<PathParser> { }
		
		val konfig: Konfig = createKonfig(dataSource, valueResolver, pathParser)
			.getSection(CommonPath(listOf("foo")))
		
		val value = konfig.get<String>(CommonPath(listOf("bar")), type)
		
		assertThat(value)
			.isEqualTo("Hello!")
	}
}
