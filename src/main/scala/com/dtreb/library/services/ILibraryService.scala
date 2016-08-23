package com.dtreb.library.services

import com.dtreb.library.models.Book
import com.twitter.finagle.exp.mysql.Result
import com.twitter.util.Future

trait ILibraryService {
  def find(id: Long): Future[Option[Book]]
  def findByTitle(title: String): Future[Option[Book]]
  def find(): Future[List[Book]]
  def create(book: Book): Future[Result]
  def update(book: Book): Future[Result]
  def delete(id: Long): Future[Result]
}
