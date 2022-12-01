package me.wietlol.konfig.api

import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class KonfigFunctionsTest : LocalTestModule()
{
	@Test
	fun `assert that Konfig get returns null when asked for a nullable type`() = unitTest {
		val path = mock<Path> { }
		val konfig = mock<Konfig> {
			on { get<String?>(any(), any()) } doReturn (null as String?)
		}
		
		val value = konfig.get<String?>(path)
		
		assertThat(value)
			.isNull()
	}
	
	@Test
	fun `assert that Konfig get throws an exception when asked for a non-nullable type`() = unitTest {
		val path = mock<Path> { }
		val konfig = mock<Konfig> {
			on { get<String?>(any(), any()) } doReturn (null as String?)
		}
		
		assertThrows<MissingRequiredValueException> { konfig.get<String>(path) }
			.also {
				it.property { ::message }
					.isEqualTo("Missing required value in configuration. null")
			}
			.property { ::valuePath }
			.isEqualTo(path)
	}
	
	@Test
	fun `assert that Konfig get returns the value when using a filled Konfig`() = unitTest {
		val path = mock<Path> { }
		val konfig = mock<Konfig> {
			on { get<String?>(any(), any()) } doReturn "Hello!"
		}
		
		val value = konfig.get<String>(path)
		
		assertThat(value)
			.isEqualTo("Hello!")
	}
	
	@Test
	fun `assert that Konfig returns null when asked for a nullable complex type`() = unitTest {
		val path = mock<Path> { }
		val konfig = mock<Konfig> {
			on { get<String?>(any(), any()) } doAnswer {
				throw InstantiationFailedException("Cannot construct instance of type 'java.lang.String'.", MissingRequiredValueException(path), emptyList())
			}
		}
		
		val value = konfig.get<String?>(path)
		
		assertThat(value)
			.isNull()
	}
	
	@Test
	fun `assert that Konfig get throws an exception when asked for a non-nullable complex type`() = unitTest {
		val path = mock<Path> { }
		val konfig = mock<Konfig> {
			on { get<String?>(any(), any()) } doAnswer {
				throw InstantiationFailedException("Cannot construct instance of type 'java.lang.String'.", MissingRequiredValueException(path), emptyList())
			}
		}
		
		assertThrows<MissingRequiredValueException> { konfig.get<String>(path) }
			.property { ::valuePath }
			.isEqualTo(path)
	}
}
