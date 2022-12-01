package me.wietlol.konfig.core

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.InstantiationFailedException
import me.wietlol.konfig.api.KonfigName
import me.wietlol.konfig.api.MissingKonfigNameAnnotationException
import me.wietlol.konfig.api.MissingRequiredValueException
import me.wietlol.konfig.api.Path
import me.wietlol.konfig.api.ValueResolver
import me.wietlol.loggo.core.loggers.NoOpLogger
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class CommonValueResolverTests : LocalTestModule()
{
	private fun createValueResolver(): ValueResolver =
		CommonValueResolver(CommonSimpleValueResolver(NoOpLogger()), NoOpLogger())
	
	@Test
	fun `assert that CommonValueResolver can return strings from a string DataSource`() = unitTest {
		val resolver: ValueResolver = createValueResolver()
		
		val path = mock<Path> { }
		val dataSource = mock<DataSource> {
			on { resolve(any()) } doReturn "Hello, World!"
		}
		
		val value: String? = resolver.resolve(path, dataSource, String::class)
		
		assertThat(value)
			.isEqualTo("Hello, World!")
	}
	
	@Test
	fun `assert that CommonValueResolver can return strings from an integral DataSource`() = unitTest {
		val resolver: ValueResolver = createValueResolver()
		
		val path = mock<Path> { }
		val dataSource = mock<DataSource> {
			on { resolve(any()) } doReturn 42
		}
		
		val value: String? = resolver.resolve(path, dataSource, String::class)
		
		assertThat(value)
			.isEqualTo("42")
	}
	
	@Test
	fun `assert that CommonValueResolver can return strings from a decimal DataSource`() = unitTest {
		val resolver: ValueResolver = createValueResolver()
		
		val path = mock<Path> { }
		val dataSource = mock<DataSource> {
			on { resolve(any()) } doReturn 4.2
		}
		
		val value: String? = resolver.resolve(path, dataSource, String::class)
		
		assertThat(value)
			.isEqualTo("4.2")
	}
	
	@Test
	fun `assert that CommonValueResolver can return strings from a flag DataSource`() = unitTest {
		val resolver: ValueResolver = createValueResolver()
		
		val path = mock<Path> { }
		val dataSource = mock<DataSource> {
			on { resolve(any()) } doReturn true
		}
		
		val value: String? = resolver.resolve(path, dataSource, String::class)
		
		assertThat(value)
			.isEqualTo("true")
	}
	
	@Test
	fun `assert that CommonValueResolver returns null from an empty DataSource`() = unitTest {
		val resolver: ValueResolver = createValueResolver()
		
		val path = mock<Path> { }
		val dataSource = mock<DataSource> {
			on { resolve(any()) } doReturn null
		}
		
		val value: String? = resolver.resolve(path, dataSource, String::class)
		
		assertThat(value)
			.isNull()
	}
	
	@Test
	fun `assert that CommonValueResolver can build instances of 'custom' classes`() = unitTest {
		data class Test(@KonfigName("name") val name: String, @KonfigName("id") val id: Int)
		
		val resolver: ValueResolver = createValueResolver()
		
		val path = mock<Path> {
			on { nodes } doReturn listOf("foo")
			on { plus(any<String>()) } doReturn mock
			on { plus(any<Path>()) } doReturn mock
		}
		val dataSource = mock<DataSource> {
			on { resolve(any()) } doReturn 42
		}
		
		val value: Test? = resolver.resolve(path, dataSource, Test::class)
		
		assertThat(value)
			.isEqualTo(Test("42", 42))
	}
	
	@Test
	fun `assert that CommonValueResolver builds instances of 'custom' classes where the properties are children of the Path`() = unitTest {
		data class Test(@KonfigName("name") val name: String, @KonfigName("id") val id: Int)
		
		val resolver: ValueResolver = createValueResolver()
		
		val path = CommonPath(listOf("foo"))
		val dataSource = mock<DataSource> {
			on { resolve(CommonPath(listOf("foo", "name"))) } doReturn "Hello!"
			on { resolve(CommonPath(listOf("foo", "id"))) } doReturn 42
		}
		
		val value: Test? = resolver.resolve(path, dataSource, Test::class)
		
		assertThat(value)
			.isEqualTo(Test("Hello!", 42))
	}
	
	@Test
	fun `assert that CommonValueResolver builds instances of 'custom' classes where the properties are nullable`() = unitTest {
		data class Test(
			@KonfigName("name") val name: String?,
			@KonfigName("id") val id: Int?
		)
		
		val resolver: ValueResolver = createValueResolver()
		
		val path = mock<Path> {
			on { nodes } doReturn listOf("foo")
			on { plus(any<String>()) } doReturn mock
			on { plus(any<Path>()) } doReturn mock
		}
		val dataSource = mock<DataSource> {
			on { resolve(any()) } doReturn null
		}
		
		val value: Test? = resolver.resolve(path, dataSource, Test::class)
		
		assertThat(value)
			.isEqualTo(Test(null, null))
	}
	
	@Test
	fun `assert that CommonValueResolver builds no instances of 'custom' classes where the properties are null but are not nullable`() = unitTest {
		data class Test(
			@KonfigName("name") val name: String
		)
		
		val resolver: ValueResolver = createValueResolver()
		
		val path = CommonPath(listOf("foo"))
		val dataSource = mock<DataSource> {
			on { resolve(any()) } doReturn null
		}
		
		assertThrows<InstantiationFailedException> { resolver.resolve<Test>(path, dataSource, Test::class) }
			.property { ::errors }
			.map { it[0] }
			.isInstanceOf<MissingRequiredValueException>()
			.property { ::message }
			.isNotNull()
			.isEqualTo("Missing required value in configuration. foo.name")
	}
	
	@Test
	fun `assert that CommonValueResolver actively rejects classes without the use of the @KonfigName annotation`() = unitTest {
		data class Test(
			val name: String
		)
		
		val resolver: ValueResolver = createValueResolver()
		
		val path = mock<Path> {
			on { nodes } doReturn listOf("foo")
			on { plus(any<String>()) } doReturn mock
			on { plus(any<Path>()) } doReturn mock
		}
		val dataSource = mock<DataSource> {
			on { resolve(any()) } doReturn null
		}
		
		assertThrows<InstantiationFailedException> { resolver.resolve<Test>(path, dataSource, Test::class) }
			.property { ::cause }
			.isNotNull()
			.isInstanceOf<MissingKonfigNameAnnotationException>()
			.property { ::message }
			.isNotNull()
			.startsWith("Missing konfig name annotation for parameter 'arg0' for constructor 'public me.wietlol.konfig.core.")
	}
}
