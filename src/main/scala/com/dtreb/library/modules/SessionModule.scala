package com.dtreb.library.modules

import javax.inject.Singleton

import com.dtreb.library.filters.{CookieSettings, SessionFilter}
import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import com.twitter.util.Duration
import com.typesafe.config.Config

object SessionModule extends TwitterModule {
  @Singleton @Provides
  def providesSessionFilter(conf: Config) = {
    val configRoot = conf.getConfig("session");
    new SessionFilter(
      secret = configRoot.getString("secret"),
      settings = CookieSettings(
        name = configRoot.getString("cookie.name"),
        domain = configRoot.getString("cookie.domain"),
        isHttpOnly = true,
        isSecure = true,
        maxAge = Duration.Top,
        path = "/"
      )
    )
  }
}