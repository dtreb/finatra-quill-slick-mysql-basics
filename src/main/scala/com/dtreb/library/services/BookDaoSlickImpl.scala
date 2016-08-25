package com.dtreb.library.services

import java.util.Date
import javax.inject.{ Inject, Singleton }

import com.dtreb.library.models.Book
import com.dtreb.library.modules.SlickDatabaseModule.SlickDatabaseSource
import com.dtreb.library.utils.TwitterFutureOps
import com.twitter.util.Future
import slick.driver.MySQLDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

/**
 * Pay attention to Future transformation.
 * Our DAO layer returns twitter Future for Finatra.
 * While Slick returns default scala Future objects.
 */
@Singleton
class BookDaoSlickImpl @Inject() (db: SlickDatabaseSource) extends BookDao {

  // **************************************************************************
  // Table mapping and date converter (Slick doesn't support java.utilDate)
  implicit private def mapDate = MappedColumnType.base[java.util.Date, java.sql.Date](
    utilDate => new java.sql.Date(utilDate.getTime()),
    sqlDate => new java.util.Date(sqlDate.getTime())
  )
  private class BookTable(tag: Tag) extends Table[Book](tag, "book") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def author = column[String]("author")
    def created = column[Date]("created")(mapDate)
    def * = (id, title, author, created) <> (Book.tupled, Book.unapply)
  }
  private val books = TableQuery[BookTable]
  // **************************************************************************

  override def find(id: Long): Future[Option[Book]] = {
    TwitterFutureOps.ScalaToTwitterFuture(
      db.run {
        books.filter(_.id === id).take(1).result
      }.map(_.headOption)
    ).toTwitterFuture
  }

  override def findByTitle(title: String): Future[Option[Book]] = {
    TwitterFutureOps.ScalaToTwitterFuture(
      db.run {
        books.filter(_.title === title).take(1).result
      }.map(_.headOption)
    ).toTwitterFuture
  }

  override def find(): Future[List[Book]] = {
    TwitterFutureOps.ScalaToTwitterFuture(
      db.run(books.to[List].result)
    ).toTwitterFuture
  }

  override def create(book: Book): Future[Long] = {
    // Returns created entity
    val insertQuery = books returning books.map(_.id) into ((book, id) => book.copy(id = id))
    TwitterFutureOps.ScalaToTwitterFuture(
      for {
        createdBook <- db.run(insertQuery += Book(0, book.title, book.author, new Date()))
      } yield {
        createdBook.id
      }
    ).toTwitterFuture
  }

  override def update(book: Book): Future[Long] = {
    TwitterFutureOps.ScalaToTwitterFuture(
      db.run(books.filter(_.id === book.id)
        .map(b => (b.title, b.author))
        .update(book.title, book.author))
        .map(_ => book.id)
    ).toTwitterFuture
  }

  override def delete(id: Long): Future[Long] = {
    TwitterFutureOps.ScalaToTwitterFuture(
      db.run(books.filter(_.id === id)
        .delete)
        .map(_ => id)
    ).toTwitterFuture
  }
}
