package controllers

import play.api.mvc._
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

  def auth = Action { implicit request =>
    implicit val accessToken = isLoggedIn

    if (accessToken.isDefined) {
      Found(redirectURL).withSession(session
        + ("twitterScreenName"        -> twitter.getScreenName(accessToken.get))
        + ("twitterAccessToken"       -> accessToken.get.token)
        + ("twitterAccessTokenSecret" -> accessToken.get.secret)
        + ("twitterUserId"            -> accessToken.get.userId.toString))
    } else {
      val requestToken = twitter.getOAuthRequestToken(callbackURL) // TODO: catch TwitterException
      Found(requestToken.getAuthenticationURL)
        .withSession(session
        + ("requestToken"       -> requestToken.token)
        + ("requestTokenSecret" -> requestToken.secret)
      )
    }
  }

  def callback = Action { implicit request =>
    val verifier = request.getQueryString("oauth_verifier")
    try {
      val requestToken = new RequestToken(session.get("requestToken").get, session.get("requestTokenSecret").get)
      implicit val accessToken = twitter.getOAuthAccessToken(requestToken, verifier.get)
      Found(redirectURL).withSession(session
        + ("twitterScreenName"        -> twitter.getScreenName)
        + ("twitterAccessToken"       -> accessToken.token)
        + ("twitterAccessTokenSecret" -> accessToken.secret)
        + ("twitterUserId"            -> accessToken.userId.toString)
        - ("requestToken")
        - ("requestTokenSecret")
      )
    } catch {
      case e: Exception => InternalServerError(e.getMessage)
    }
  }

  protected def isLoggedIn(implicit request: Request[AnyContent]): Option[AccessToken] = {
    val loggedIn = for (
      userId      <- session.get("twitterUserId");
      token       <- session.get("twitterAccessToken");
      tokenSecret <- session.get("twitterAccessTokenSecret");
      accessToken <- Some(new AccessToken(token, tokenSecret, userId.toLong))
      if(!twitter.getScreenName(accessToken).isEmpty)
    ) yield {
      accessToken
    }

    loggedIn
  }
}
