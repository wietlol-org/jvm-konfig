package me.wietlol.konfig.core

import me.wietlol.konfig.api.DataSource
import me.wietlol.konfig.api.Konfig
import me.wietlol.konfig.api.KonfigBuilder
import me.wietlol.konfig.api.PathParser
import me.wietlol.konfig.api.ValueResolver
import me.wietlol.konfig.core.datasources.FlatDataSource
import me.wietlol.konfig.core.datasources.MultiDataSource
import me.wietlol.loggo.common.CommonLogger
import me.wietlol.loggo.core.loggers.NoOpLogger

class CommonKonfigBuilder : KonfigBuilder
{
	private val dataSources: MutableList<DataSource> = ArrayList()
	private lateinit var valueResolver: ValueResolver
	private lateinit var pathParser: PathParser
	private var logger: CommonLogger = NoOpLogger()
	
	override fun withValueResolver(valueResolver: ValueResolver): KonfigBuilder =
		apply { this.valueResolver = valueResolver }
	
	override fun withPathParser(pathParser: PathParser): KonfigBuilder =
		apply { this.pathParser = pathParser }
	
	override fun addSource(dataSource: DataSource): KonfigBuilder =
		apply { dataSources.add(dataSource) }
	
	override fun withLogger(logger: CommonLogger): KonfigBuilder =
		apply { this.logger = logger }
	
	private fun compressDataSources(): DataSource =
		MultiDataSource(dataSources.toList())
			.let { FlatDataSource.from(it) }
	
	override fun build(): Konfig =
		RootKonfig(compressDataSources(), valueResolver, pathParser, logger)
}
