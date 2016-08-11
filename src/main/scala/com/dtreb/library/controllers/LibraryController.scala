package com.dtreb.library.controllers

import java.util.Date
import javax.inject.{ Inject, Singleton }

import com.dtreb.library.models.Book
import com.dtreb.library.services.LibraryService
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

@Singleton
class LibraryController @Inject() (
    libraryService: LibraryService
) extends Controller {

  get("/book/:id") { request: Request =>
    for {
      book <- libraryService.find(request.getLongParam("id"))
    } yield {
      book
    }
  }

  get("/book") { request: Request =>
    for {
      books <- libraryService.find
    } yield {
      books
    }
  }

  get("/:*") { request: Request =>
    response.ok.fileOrIndex(
      request.params("*"),
      "index.html"
    )
  }

  post("/book") { request: Request =>
    for {
      result <- libraryService.create(
        new Book(
          request.getParam("title"),
          request.getParam("author"),
          new Date()
        )
      )
    } yield {
      response.ok
    }
  }

  post("/book/:id") { request: Request =>
    for {
      result <- libraryService.update(
        new Book(
          request.getLongParam("id"),
          request.getParam("title"),
          request.getParam("author")
        )
      )
    } yield {
      response.ok
    }
  }

  delete("/book/:id") { request: Request =>
    for {
      book <- libraryService.delete(request.getLongParam("id"))
    } yield {
      response.ok
    }
  }
}
