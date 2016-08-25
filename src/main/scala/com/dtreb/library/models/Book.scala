package com.dtreb.library.models

import java.util.Date

case class Book(
  id: Long,
  title: String,
  author: String,
  created: Date
)