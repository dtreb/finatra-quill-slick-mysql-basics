package com.dtreb.library.services

import java.util.Date
import javax.inject.{ Inject, Singleton }

import com.dtreb.library.models.Book
import com.dtreb.library.modules.QuillDatabaseModule.QuillDatabaseSource
import com.twitter.finagle.exp.mysql.{ Error, OK, Result }
import com.twitter.util.Future
import io.getquill._

@Singleton
class BookDaoQuillImpl @Inject() (db: QuillDatabaseSource) extends BookDao {

  override def find(id: Long): Future[Option[Book]] = {
    val q = quote { (id: Long) =>
      query[Book]
        .filter(i => i.id == id)
        .take(1)
    }
    db.run(q)(id).map(_.headOption)
  }

  override def findByTitle(title: String): Future[Option[Book]] = {
    val q = quote { (title: String) =>
      query[Book]
        .filter(i => i.title == title)
        .take(1)
    }
    db.run(q)(title).map(_.headOption)
  }

  override def find(): Future[List[Book]] = {
    val q = quote {
      query[Book]
    }
    db.run(q)
  }

  override def create(book: Book): Future[Long] = {
    val a = quote {
      (title: String, author: String, created: Date) =>
        query[Book]
          .insert(
            _.title -> title,
            _.author -> author,
            _.created -> created
          )
    }
    checkResult(
      db.run(a)(List((book.title, book.author, new Date()))).map(_.head),
      Option.empty
    )
  }

  override def update(book: Book): Future[Long] = {
    val a = quote {
      (id: Long, title: String, author: String) =>
        query[Book]
          .filter(b => b.id == id)
          .update(
            _.title -> title,
            _.author -> author
          )
    }
    checkResult(
      db.run(a)(List((book.id, book.title, book.author))).map(_.head),
      Option(book.id)
    )
  }

  override def delete(id: Long): Future[Long] = {
    val a = quote {
      (id: Long) =>
        query[Book]
          .filter(p => p.id == id)
          .delete
    }
    checkResult(
      db.run(a)(List(id)).map(_.head),
      Option(id)
    )
  }

  /**
   * Checks query result and returns correspondent record Id.
   * @param result raw Result
   * @param id Optional Id param to return in case of successful result (makes sense on update, delete).
   *           Skip param value if it's fine to use result insert Id (for create)
   * @return correspondent record Id
   */
  private def checkResult(result: Future[Result], id: Option[Long]): Future[Long] = {
    result.flatMap(r => r match {
      case r: OK => Future { id.getOrElse(r.insertId) }
      case r: Error => throw new Exception(r.message)
      case _ => throw new Exception("Unrecognized error!")
    })
  }
}
