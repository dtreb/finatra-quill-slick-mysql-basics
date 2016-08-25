package com.dtreb.library.services

import com.dtreb.library.models.Book
import com.twitter.util.Future

trait BookDao {
  def find(id: Long): Future[Option[Book]]
  def findByTitle(title: String): Future[Option[Book]]
  def find(): Future[List[Book]]
  def create(book: Book): Future[Long]
  def update(book: Book): Future[Long]
  def delete(id: Long): Future[Long]
}
