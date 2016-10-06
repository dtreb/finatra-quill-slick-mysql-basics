package com.dtreb.library

import com.dtreb.library.models.Book
import com.dtreb.library.services.BookDaoQuillImpl
import com.google.inject.testing.fieldbinder.Bind
import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.Mockito
import com.twitter.inject.server.FeatureTest
import com.twitter.util.Future
import org.mockito.Matchers._
import org.mockito.Mockito._;

// TODO: add failure tests
class LibraryFeatureQuillTest extends FeatureTest with Mockito {
  override val server = new EmbeddedHttpServer(new LibraryServer)
  @Bind val dao = smartMock[BookDaoQuillImpl]

  // Uncomment to test Slick instead of Quill
  // Do the same in src/main/scala/com/dtreb/library/controllers/LibraryController.scala
  // @Bind val dao = smartMock[BookDaoSlickImpl]

  // Warmup
  dao.find(1) returns Future(None)

  val result: Future[Long] = Future { 1 };
  val book = Book(1, "title", "author", null);

  override def beforeEach() {
    reset(dao);
    dao.find(1) returns Future {
      Option(book)
    }
  }

  "book create" in {
    dao.create(anyObject()) returns result
    server.httpPost(
      path = "/book",
      postBody = "title=title&author=author",
      andExpect = Ok
    )
    verify(dao).find(1)
  }

  "book read" in {
    server.httpGet(
      path = "/book/1",
      andExpect = Ok
    )
    verify(dao).find(1)
  }

  "book update" in {
    dao.update(anyObject()) returns result
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
    verify(dao).find(1)
  }

  "book delete" in {
    dao.delete(1) returns result
    server.httpDelete(
      path = "/book/1",
      andExpect = Ok
    )
  }
}
