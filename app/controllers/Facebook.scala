package controllers

import play.api.mvc._
import com.socialorra.api.registry.FacebookComponentRegistry

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

  def callback = Action { implicit request =>
    val oauthCode = request.getQueryString("code")
    try {
      implicit val accessToken = facebook.getOAuthAccessToken(oauthCode.get, callbackURL)
      Found(redirectURL).withSession(session + ("facebookScreenName" -> facebook.getName))
    } catch {
      case e: Exception => InternalServerError(e.getMessage)
    }
  }

}
