package me.wietlol.konfig.api

import me.wietlol.loggo.common.CommonLogger

interface KonfigBuilder
{
	fun withValueResolver(valueResolver: ValueResolver): KonfigBuilder
	
	fun withPathParser(pathParser: PathParser): KonfigBuilder
	
	fun addSource(dataSource: DataSource): KonfigBuilder
	
	fun withLogger(logger: CommonLogger): KonfigBuilder
	
	fun build(): Konfig
}
