package controllers

import play.api.mvc._
import com.socialorra.api.registry.FacebookRegistry
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import com.socialorra.api.repo.facebook.AccessToken


object Facebook extends Controller {

  val facebook = FacebookRegistry.facebookRepository

  val serverURL = play.api.Play.current.configuration.getString("server.url").get
  lazy val callbackURL = s"$serverURL/facebook/callback"
  lazy val redirectURL = s"$serverURL/facebook/testLogin"
  lazy val authURL     = s"$serverURL/facebook/auth"

  lazy val userInfoURL = s"$serverURL/facebook/userInfo"
  lazy val infoURL = s"$serverURL/facebook/testInfo"
  lazy val infoAuthURL     = s"$serverURL/facebook/infoAuth"

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
        Found(redirectURL).withSession(request.session + ("facebookScreenName" -> screenName))
      }

    } catch {
      case e: Exception => Future { InternalServerError(e.getMessage) }
      case x: Throwable => Future { InternalServerError("unknown error") }
    }
  }

  def testInfo = Action { implicit request =>
    if (request.session.get("facebookUserEmail").isEmpty) {
      TemporaryRedirect(infoAuthURL)
    } else {
      Ok(s"User email: /${request.session("facebookUserEmail")}!")
    }

  }

  def infoAuth = Action { implicit request =>
    Found(facebook.getOAuthAuthorizationURL(userInfoURL))
  }

  def userInfo = Action.async { implicit request =>
    try {
      val oauthCode: String = request.getQueryString("code").get

      for {
        accessToken <- facebook.getOAuthAccessToken(oauthCode, userInfoURL);
        userEmail <- facebook.getEmail(accessToken)

      } yield {
        Found(infoURL).withSession(request.session + ("facebookUserEmail" -> userEmail ))
      }
    } catch {
      case e: Exception => Future { InternalServerError(e.getMessage) }
      case x: Throwable => Future { InternalServerError("unknown error") }
    }
  }

}
