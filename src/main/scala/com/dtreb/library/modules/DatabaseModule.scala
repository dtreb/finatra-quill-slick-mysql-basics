package com.dtreb.library.modules

import com.google.inject.{ Provides, Singleton }
import com.twitter.inject.TwitterModule
import com.typesafe.config.Config
import io.getquill.naming.SnakeCase
import io.getquill.sources.finagle.mysql.FinagleMysqlSource
import io.getquill.{ FinagleMysqlSourceConfig, _ }

object DatabaseModule extends TwitterModule {

  type DatabaseSource = FinagleMysqlSource[SnakeCase]

  @Provides @Singleton
  def provideDataBaseSource(conf: Config): DatabaseSource = source(new FinagleMysqlSourceConfig[SnakeCase]("") {
    override def config = conf.getConfig("db")
  })

}
