package com.dtreb.library.modules

import com.google.inject.{ Provides, Singleton }
import com.twitter.inject.TwitterModule
import com.typesafe.config.Config
import io.getquill.{ FinagleMysqlSourceConfig, _ }
import io.getquill.naming.SnakeCase
import io.getquill.sources.finagle.mysql.FinagleMysqlSource

object QuillDatabaseModule extends TwitterModule {

  type QuillDatabaseSource = FinagleMysqlSource[SnakeCase]

  @Provides @Singleton
  def provideDataBaseSource(conf: Config): QuillDatabaseSource = source(new FinagleMysqlSourceConfig[SnakeCase]("") {
    override def config = conf.getConfig("quill.db")
  })

}
