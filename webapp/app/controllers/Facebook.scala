package controllers

import play.api.mvc._
import com.socialorra.api.registry.FacebookComponentRegistry
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import com.socialorra.api.repo.facebook.AccessToken

object Facebook extends Controller {

  val facebook = FacebookComponentRegistry.facebookService

  val serverURL = play.api.Play.current.configuration.getString("server.url").get
  lazy val callbackURL = s"$serverURL/facebook/callback"
  lazy val redirectURL = s"$serverURL/facebook/testLogin"
  lazy val authURL     = s"$serverURL/facebook/auth"

  def testLogin = Action { implicit request =>
    if (request.session.get("facebookScreenName").isEmpty) {
      TemporaryRedirect(authURL)
    } else {
      Ok(s"Howdy fb/${request.session("facebookScreenName")}!")
    }
  }

  def auth = Action { implicit request =>
    Found(facebook.getOAuthAuthorizationURL(callbackURL))
  }

  def callback = Action.async { implicit request =>
    try {
      val oauthCode: String = request.getQueryString("code").get

      for {
        accessToken <- facebook.getOAuthAccessToken(oauthCode, callbackURL);
        screenName <- facebook.getName(accessToken)
      } yield {
        Found(redirectURL).withSession(session + ("facebookScreenName" -> screenName))
      }

    } catch {
      case e: Exception => future { InternalServerError(e.getMessage) }
      case x: Throwable => future { InternalServerError("unknown error") }
    }
  }
}
