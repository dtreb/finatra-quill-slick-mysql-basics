package com.dtreb.library.models

import java.util.Date

case class Book(
    id: Long,
    title: String,
    author: String,
    created: Date
) {
  def this(title: String, author: String) = this(0, title, author, null)
  def this(title: String, author: String, created: Date) = this(0, title, author, created)
}
