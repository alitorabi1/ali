package controllers

import play.api.mvc._
import com.socialorra.api.registry.UserComponentRegistry

object Login extends Controller {

  val userService = UserComponentRegistry.userService

  def auth = Action { implicit request => {
        for {
          params <- request.body.asFormUrlEncoded
          email <- params.getOrElse("email", Seq.empty[String]).headOption
          password <- params.getOrElse("password", Seq.empty[String]).headOption
          authenticated <- Some(userService.authenticate(email, password))
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

