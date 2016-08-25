package com.dtreb.library.controllers

import java.util.Date
import javax.inject.{ Inject, Singleton }

import com.dtreb.library.models.Book
import com.dtreb.library.services.{ BookDaoQuillImpl, BookDaoSlickImpl }
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

@Singleton
class LibraryController @Inject() (
    // Uncomment to use Slick instead of Quill
    // Do the same in src/test/scala/com/dtreb/library/LibraryFeatureQuillTest.scala
    // dao: BookDaoSlickImpl
    dao: BookDaoQuillImpl
) extends Controller {

  get("/book/:id") { request: Request =>
    dao.find(request.getLongParam("id"))
  }

  get("/book") { request: Request =>
    dao.find
  }

  get("/:*") { request: Request =>
    response.ok.fileOrIndex(
      request.params("*"),
      "index.html"
    )
  }

  post("/book") { request: Request =>
    for {
      id <- dao.create(Book(
        0,
        request.getParam("title"),
        request.getParam("author"),
        new Date()
      ))
      result <- dao.find(id)
    } yield result
  }

  post("/book/:id") { request: Request =>
    for {
      id <- dao.update(Book(
        request.getLongParam("id"),
        request.getParam("title"),
        request.getParam("author"),
        null
      ))
      result <- dao.find(id)
    } yield result
  }

  delete("/book/:id") { request: Request =>
    dao.delete(request.getLongParam("id"))
  }
}
