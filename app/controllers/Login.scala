package controllers

import play.api.mvc._

object Login extends Controller {

  def auth = Action { implicit request => {
        for (
          params <- request.body.asFormUrlEncoded;
          email <- params.getOrElse("email", Seq.empty[String]).headOption;
          password <- params.getOrElse("password", Seq.empty[String]).headOption
        ) yield {

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