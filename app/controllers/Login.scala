package controllers

import play.api.mvc._

object Login extends Controller with UserModule {

  def auth = Action { implicit request => {
        for {
          params <- request.body.asFormUrlEncoded
          email <- params.getOrElse("email", Seq.empty[String]).headOption
          password <- params.getOrElse("password", Seq.empty[String]).headOption
          authenticated <- Some(authenticator.authenticate(email, password))
          if (authenticated)
        } yield {

          val r = params.getOrElse("next", Seq.empty[String]).headOption match {
            case Some(redirect) => Found(redirect)
            case None => NoContent
          }

          r.withSession("email" -> email)

        }
      } getOrElse {
    Unauthorized
  }}
}

trait UserModule {

  val authenticator: AuthenticatorModule = FakeAuthenticatorModule
}

trait AuthenticatorModule {

  def authenticate(email: String, password: String): Boolean
}

object FakeAuthenticatorModule extends AuthenticatorModule {

  val PASS = "password"

  def authenticate(email: String, password: String): Boolean = {
    password == PASS
  }
}
