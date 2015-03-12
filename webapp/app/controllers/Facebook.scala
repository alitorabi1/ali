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

  lazy val postURL     = s"$serverURL/facebook/testPost"
  lazy val dataAuthURL    = s"$serverURL/facebook/dataAuth"
  lazy val dataPostURL    = s"$serverURL/facebook/dataPost"


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
        postID <- facebook.postMessage(accessToken)

      } yield {
        Found(redirectURL).withSession(request.session + ("facebookScreenName" -> screenName))
      }

    } catch {
      case e: Exception => Future {
        InternalServerError(e.getMessage)
      }
      case x: Throwable => Future {
        InternalServerError("unknown error")
      }
    }
  }


    def testPost = Action { implicit request =>
      if (request.session.get("FBMsgId").isEmpty) {
        request.session - "FBMsgId"
        TemporaryRedirect(dataAuthURL)
      } else {
        Ok(s"Hello fb ${request.session("FBMsgId")}")
      }
    }

    def dataAuth = Action {implicit request =>
      Found(facebook.getOAuthAuthorizationURL(dataPostURL))
    }

    def dataPost = Action.async { implicit request =>
      try{
        val oauthCode: String = request.getQueryString("code").get
        for {
          accessToken <- facebook.getOAuthAccessToken(oauthCode, dataPostURL);
          postMsgId <- facebook.postMessage(accessToken)
        } yield{
          Found(postURL).withSession(request.session + ("FBMsgId" -> postMsgId))
        }
      } catch {
        case e: Exception => Future {InternalServerError(e.getMessage)}
      }
    }
}
