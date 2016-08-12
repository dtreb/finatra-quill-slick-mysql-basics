package com.dtreb.library

import com.dtreb.library.services.LibraryService
import com.google.inject.testing.fieldbinder.Bind
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.Mockito
import com.twitter.inject.server.FeatureTest

class LibraryFeatureTest extends FeatureTest with Mockito {
  override val server = new EmbeddedHttpServer(new LibraryServer)

  @Bind val libraryService = smartMock[LibraryService]

  // TODO
}
