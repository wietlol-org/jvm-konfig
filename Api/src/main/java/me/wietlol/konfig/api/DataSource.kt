package me.wietlol.konfig.api

interface DataSource
{
	fun resolve(path: Path): Any?
	
	fun allValues(): Sequence<Pair<Path, Any?>>
}
