package controllers

import play.api._
import play.api.mvc._
import utils.security.Secured

object Login extends Controller {

  def index = Action {
    Ok(views.html.login())
  }

  def auth = Action { implicit request =>
      val r = for (
          params   <- request.body.asFormUrlEncoded;
          email    <- params.getOrElse("email", Seq.empty[String]).headOption;
          password <- params.getOrElse("password", Seq.empty[String]).headOption
        ) yield {
          Redirect(routes.Home.index()).withSession("email" -> email)
        }

      val result = r.getOrElse {
        Unauthorized
      }

      result
  }
}