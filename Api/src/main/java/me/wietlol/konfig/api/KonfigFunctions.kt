package me.wietlol.konfig.api

inline fun <reified T> Konfig.get(path: String): T =
	get(parse(path))

inline fun <reified T> Konfig.get(path: Path): T
{
	val value = handleMissingProperties {
		get<T>(path, T::class)
	}
		.also { it.handleNullRequires(path) }
	
	return value as T
}

inline fun <reified T> T?.handleNullRequires(path: Path): T? =
	this
		?: try
		{
			null as T
		}
		catch (ex: NullPointerException)
		{
			throw MissingRequiredValueException("Missing required value in configuration.", ex, path)
		}

fun <T> handleMissingProperties(supplier: () -> T): T? =
	try
	{
		supplier()
	}
	catch (ex: InstantiationFailedException)
	{
		val cause = ex.cause
		if (cause is MissingRequiredValueException)
			null
		else
			throw ex
	}
