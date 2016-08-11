package com.dtreb.library

import com.dtreb.library.services.LibraryService
import com.google.inject.testing.fieldbinder.Bind
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.Mockito
import com.twitter.inject.server.FeatureTest
import com.twitter.util.Future

class LibraryFeatureTest extends FeatureTest with Mockito {
  override val server = new EmbeddedHttpServer(new LibraryServer)

  @Bind val libraryService = smartMock[LibraryService]

  /* Mock GET Request performed by warmup */
  libraryService.find(1) returns Future(None)

  // TODO
}
