package com.dtreb.library.services

import java.util.Date
import javax.inject.{ Inject, Singleton }

import com.dtreb.library.models.Book
import com.dtreb.library.modules.QuillDatabaseModule.QuillDatabaseSource
import com.twitter.finagle.exp.mysql.Result
import com.twitter.util.Future
import io.getquill._

@Singleton
class LibraryService @Inject() (db: QuillDatabaseSource) {

  def find(id: Long): Future[Option[Book]] = {
    val q = quote { (id: Long) =>
      query[Book].filter(i => i.id == id).take(1)
    }
    db.run(q)(id).map(_.headOption)
  }

  def findByTitle(title: String): Future[Option[Book]] = {
    val q = quote { (title: String) =>
      query[Book].filter(i => i.title == title).take(1)
    }
    db.run(q)(title).map(_.headOption)
  }

  def find(): Future[List[Book]] = {
    val q = quote {
      query[Book]
    }
    db.run(q)
  }

  def create(book: Book): Future[Result] = {
    val a = quote {
      (title: String, author: String, created: Date) =>
        query[Book].insert(
          _.title -> title,
          _.author -> author,
          _.created -> created
        )
    }
    db.run(a)(List((book.title, book.author, new Date()))).map(_.head)
  }

  def update(book: Book): Future[Result] = {
    val a = quote {
      (id: Long, title: String, author: String) =>
        query[Book].filter(b => b.id == id).update(
          _.title -> title,
          _.author -> author
        )
    }
    db.run(a)(List((book.id, book.title, book.author))).map(_.head)
  }

  def delete(id: Long): Future[Result] = {
    val a = quote {
      (id: Long) =>
        query[Book].filter(p => p.id == id).delete
    }
    db.run(a)(List(id)).map(_.head)
  }
}
