package com.dtreb.library.filters

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import com.dtreb.library.utils.{MessageEncryptorPool, Utils}
import com.twitter.finagle.http.{Cookie, Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.util.{Duration, Future}

case class CookieSettings(name: String,
                          domain: String = null,
                          isHttpOnly: Boolean = false,
                          isSecure: Boolean = false,
                          maxAge: Duration = Duration.Top,
                          path: String)

class SessionFilter(secret: String, settings: CookieSettings) extends SimpleFilter[Request, Response] {

  val encPool = new MessageEncryptorPool(secret)

  override def apply(req: Request, service: Service[Request, Response]): Future[Response] = {

    // Here you can check DB for credentials or existing session,
    // persist new one etc.

    readSession(req)

    service(req).map { resp =>
      writeSession(resp)
      resp
    }
  }

  private def readSession(req: Request): Unit = {
    val s = req.cookies.get(settings.name).map { cookie =>
      val is = new ByteArrayInputStream(encPool.get.decryptAndVerify(cookie.value))
      Utils.using(new ObjectInputStream(is)) { ois =>
        ois.readObject.asInstanceOf[Map[String, String]]
      }
    } getOrElse Map.empty[String, String]
    req.ctx.update(SessionContext.ReqSessionField, s)
  }

  private def writeSession(resp: Response) = {
    val os = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(os)
    oos.writeObject(resp.ctx.apply(SessionContext.RespSessionField))
    oos.close()
    val s = encPool.get.encryptAndSign(os.toByteArray)
    val cookie = new Cookie(settings.name, s)
    cookie.domain = settings.domain
    cookie.httpOnly = settings.isHttpOnly
    cookie.isSecure = settings.isSecure
    cookie.maxAge = settings.maxAge
    cookie.path = settings.path
    resp.cookies.add(cookie)
    resp
  }
}

object SessionContext {
  val ReqSessionField = Request.Schema.newField[Map[String, String]](Map.empty[String, String])
  val RespSessionField = Response.Schema.newField[Map[String, String]](Map.empty[String, String])

  implicit class RequestExtensions(val req: Request) extends AnyVal {
    def session: Map[String, String] = req.ctx(ReqSessionField)
  }

  implicit class EnrichedResponseExtensions(val resp: ResponseBuilder#EnrichedResponse) extends AnyVal {
    def session(newSession: Map[String, String]): ResponseBuilder#EnrichedResponse = {
      resp.ctx.update(RespSessionField, newSession)
      resp
    }
  }

  implicit class ResponseExtensions(val resp: Response) extends AnyVal {
    def setSession(newSession: Map[String, String]): Unit = {
      resp.ctx.update(RespSessionField, newSession)
    }
  }
}
