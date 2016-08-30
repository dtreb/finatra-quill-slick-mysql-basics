package com.dtreb.library.utils

import java.io.Closeable

object Utils {
  def using[A <: Closeable, B](closeable: A)(f: A => B) = {
    try {
      f(closeable)
    } finally {
      closeable.close()
    }
  }
}
