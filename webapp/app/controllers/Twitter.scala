package controllers

import play.api.mvc._

import scala.concurrent._
import ExecutionContext.Implicits.global
import com.socialorra.api.registry.TwitterComponentRegistry
import com.socialorra.api.repo.twitter.{AccessToken, RequestToken}

object Twitter extends Controller {

  val twitter = TwitterComponentRegistry.twitterService

  val serverURL = play.api.Play.current.configuration.getString("server.url").get
  lazy val callbackURL = s"$serverURL/twitter/callback"
  lazy val redirectURL = s"$serverURL/twitter/testLogin"
  lazy val authURL     = s"$serverURL/twitter/auth"

  def testLogin = Action { implicit request =>
    if (request.session.get("twitterScreenName").isEmpty) {
      TemporaryRedirect(authURL)
    } else {
      Ok(s"Howdy @${request.session("twitterScreenName")}!")
    }
  }

  def auth = Action.async { implicit request =>

    val r = for {
      accessToken <- isLoggedIn; if (accessToken.isDefined);
      screenName  <- twitter.getScreenName(accessToken.get)
    } yield {
      Found(redirectURL).withSession(session
        + ("twitterScreenName"        -> screenName)
        + ("twitterAccessToken"       -> accessToken.get.token)
        + ("twitterAccessTokenSecret" -> accessToken.get.secret)
        + ("twitterUserId"            -> accessToken.get.userId.toString))
    }

    r fallbackTo (twitter.getOAuthRequestToken(callbackURL) map { requestToken =>
        // TODO: catch TwitterException
        Found(requestToken.getAuthenticationURL)
          .withSession(session
          + ("requestToken"       -> requestToken.token)
          + ("requestTokenSecret" -> requestToken.secret))
    })
  }

  def callback = Action.async { implicit request =>

    val verifier = request.getQueryString("oauth_verifier")
    val requestToken = new RequestToken(session.get("requestToken").get, session.get("requestTokenSecret").get)

    for {
      accessToken <- twitter.getOAuthAccessToken(requestToken, verifier.get);
      screenName  <- twitter.getScreenName(accessToken)
    } yield {
      Found(redirectURL).withSession(session
        + ("twitterScreenName"        -> screenName)
        + ("twitterAccessToken"       -> accessToken.token)
        + ("twitterAccessTokenSecret" -> accessToken.secret)
        + ("twitterUserId"            -> accessToken.userId.toString)
        - ("requestToken")
        - ("requestTokenSecret")
      )
    }
  }

  protected def isLoggedIn(implicit request: Request[AnyContent]): Future[Option[AccessToken]] = {
    val loggedIn = for (
      userId      <- session.get("twitterUserId");
      token       <- session.get("twitterAccessToken");
      tokenSecret <- session.get("twitterAccessTokenSecret");
      accessToken <- Some(new AccessToken(token, tokenSecret, userId.toLong))
    ) yield {
        twitter.getScreenName(accessToken) map { screenName =>
          if (screenName.isEmpty) None
          else Some(accessToken)
        }
    }

    loggedIn getOrElse future { None }
  }
}
