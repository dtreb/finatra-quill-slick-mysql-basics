package com.dtreb.library.controllers

import java.util.Date
import javax.inject.{ Inject, Singleton }

import com.dtreb.library.models.Book
import com.dtreb.library.services.QuillLibraryService
import com.twitter.finagle.exp.mysql.OK
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Future

@Singleton
class LibraryController @Inject() (
    libraryService: QuillLibraryService
) extends Controller {

  get("/book/:id") { request: Request =>
    libraryService.find(request.getLongParam("id"))
  }

  get("/book") { request: Request =>
    libraryService.find
  }

  get("/:*") { request: Request =>
    response.ok.fileOrIndex(
      request.params("*"),
      "index.html"
    )
  }

  post("/book") { request: Request =>
    libraryService.create(
      new Book(
        request.getParam("title"),
        request.getParam("author"),
        new Date()
      )
    ).flatMap(r => r match {
        case r: OK => libraryService.find(r.insertId)
        case r => Future { r } // TODO: handle exceptions properly
      })
  }

  post("/book/:id") { request: Request =>
    libraryService.update(
      new Book(
        request.getLongParam("id"),
        request.getParam("title"),
        request.getParam("author")
      )
    ).flatMap(r => r match {
        case r: OK => libraryService.find(request.getLongParam("id"))
        case r => Future { r } // TODO: handle exceptions properly
      })
  }

  delete("/book/:id") { request: Request =>
    libraryService.delete(request.getLongParam("id")).flatMap(r => r match {
      case r: OK => Future { request.getLongParam("id") }
      case r => Future { r } // TODO: handle exceptions properly
    })
  }
}
