package com.dtreb.library

import com.dtreb.library.controllers.LibraryController
import com.dtreb.library.filters.SessionFilter
import com.dtreb.library.modules._
import com.dtreb.library.warmup.LibraryWarmupHandler
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter

object LibraryServerMain extends LibraryServer

class LibraryServer extends HttpServer {
  override def modules = Seq(
    TypesafeConfigModule,
    QuillDatabaseModule,
    SlickDatabaseModule,
    SessionModule
  )

  override def jacksonModule = CustomJacksonModule

  override def defaultFinatraHttpPort = ":9999"

  override def configureHttp(router: HttpRouter) {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[SessionFilter, LibraryController]
  }

  override def warmup() {
    handle[LibraryWarmupHandler]()
  }

}
