package com.dtreb.library.warmup

import javax.inject.{Inject, Singleton}

import com.twitter.finatra.http.routing.HttpWarmup
import com.twitter.finatra.httpclient.RequestBuilder._
import com.twitter.inject.utils.Handler

@Singleton
class LibraryWarmupHandler @Inject() (
    httpWarmup: HttpWarmup
) extends Handler {

  override def handle(): Unit = {
    httpWarmup.send(
      get("/book/1"),
      times = 5
    )
  }
}
