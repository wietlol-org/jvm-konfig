package me.wietlol.konfig.api

interface SimpleValueConverter<T>
{
	fun convert(value: Any?): T?
}
