package com.dtreb.library

import com.dtreb.library.controllers.LibraryController
import com.dtreb.library.modules.{ CustomJacksonModule, DatabaseModule, TypesafeConfigModule }
import com.dtreb.library.warmup.LibraryWarmupHandler
import com.twitter.finagle.http.{ Request, Response }
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{ CommonFilters, LoggingMDCFilter, TraceIdMDCFilter }
import com.twitter.finatra.http.routing.HttpRouter

object LibraryServerMain extends LibraryServer

class LibraryServer extends HttpServer {
  override def modules = Seq(
    TypesafeConfigModule,
    DatabaseModule
  )

  override def jacksonModule = CustomJacksonModule

  override def defaultFinatraHttpPort = ":9999"

  override def configureHttp(router: HttpRouter) {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[LibraryController]
  }

  override def warmup() {
    handle[LibraryWarmupHandler]()
  }

}
