package com.dtreb.library

import com.dtreb.library.models.Book
import com.dtreb.library.services.LibraryService
import com.google.inject.testing.fieldbinder.Bind
import com.twitter.finagle.exp.mysql.{ OK, Result }
import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.Mockito
import com.twitter.inject.server.FeatureTest
import com.twitter.util.Future
import org.mockito.Matchers._
import org.mockito.Mockito._;

// TODO: add failure tests
class LibraryFeatureTest extends FeatureTest with Mockito {
  override val server = new EmbeddedHttpServer(new LibraryServer)
  @Bind val libraryService = smartMock[LibraryService]

  // Warmup
  libraryService.find(1) returns Future(None)

  val okResult: Result = new OK(1, 1, 1, 0, "Message");
  val book = new Book("title", "author");

  override def beforeEach() {
    reset(libraryService);
    libraryService.find(1) returns Future {
      Option(book)
    }
  }

  "book create" in {
    libraryService.create(anyObject()) returns Future {
      okResult
    }
    server.httpPost(
      path = "/book",
      postBody = "title=title&author=author",
      andExpect = Ok
    )
    verify(libraryService).find(1)
  }

  "book read" in {
    server.httpGet(
      path = "/book/1",
      andExpect = Ok
    )
    verify(libraryService).find(1)
  }

  "book update" in {
    libraryService.update(anyObject()) returns Future {
      okResult
    }
    server.httpPost(
      path = "/book/1",
      postBody = """
          {
            "id": 1,
            "title": "title",
            "author": "author"
          }
          """,
      andExpect = Ok
    )
    verify(libraryService).find(1)
  }

  "book delete" in {
    libraryService.delete(1) returns Future {
      okResult
    }
    server.httpDelete(
      path = "/book/1",
      andExpect = Ok
    )
  }
}
