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
  lazy val dataPostURL = s"$serverURL/facebook/dataPost"
  lazy val dataAuthURL = s"$serverURL/facebook/dataAuth"
  
  lazy val testUserDetailsURL = s"$serverURL/facebook/testUserDetails"
  lazy val userDetailsAuthURL = s"$serverURL/facebook/userDetailsAuth"
  lazy val userDetailsURL = s"$serverURL/facebook/getUserDetails"
  
  lazy val testPostDetailsURL = s"$serverURL/facebook/testPostDetails"
  lazy val postDetailsAuthURL = s"$serverURL/facebook/postDetailsAuth"
  lazy val postDetailsURL = s"$serverURL/facebook/getPostDetails"

  def testLogin = Action { implicit request =>
    if (request.session.get("facebookScreen").isEmpty) {
      TemporaryRedirect(authURL)
    } else {
      Ok(s"Howdy fb/${request.session("facebookScreen")}!")
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
        Found(redirectURL).withSession(request.session + ("facebookScreen" -> screenName /*+ "FBMsgId" -> postMsgId*/))
      }

    } catch {
      case e: Exception => Future { InternalServerError(e.getMessage) }
      case x: Throwable => Future { InternalServerError("unknown error") }
    }
  }

  // Method to post messages in FB
  def testPost = Action {implicit request =>
    if (request.session.get("FBMsgId").isEmpty){
      request.session - "FBMsgId"
      TemporaryRedirect(dataAuthURL)
    } else {
      Ok(s"Post Id's Message: ${request.session("FBMsgId")}")
    }
  }

  def dataAuth = Action {implicit request =>
    Found(facebook.getOAuthAuthorizationURL(dataPostURL))
  }

  def dataPost =  Action.async { implicit request =>
    try{
      val oauthCode: String = request.getQueryString("code").get

      for {
        accessToken <- facebook.getOAuthAccessToken(oauthCode, dataPostURL);
        postMsgId <- facebook.postMessages(accessToken)
//        postMsg <- facebook.getPostMessage(accessToken)
//        userDetails <- facebook.getUserDetails(accessToken)
      } yield{
        Found(postURL).withSession(request.session + ("FBMsgId" -> postMsgId))
      }
    } catch {
      case e: Exception => Future {InternalServerError(e.getMessage)}
      //     case x: Throwable => Future { InternalServerError("unknown error") }
    }

  }
  
  // Method to get user details from FB
  def testUserDetails = Action {implicit request=>
    if (request.session.get("UserDetails").isEmpty){
      TemporaryRedirect(userDetailsAuthURL)      
    } else {
      Ok(s"User Details: \r\n ${request.session("UserDetails")}")
    }
  }
  
  def userDetailsAuth = Action { implicit request =>
    Found(facebook.getOAuthAuthorizationURL(userDetailsURL))    
  }
  
  def getUserDetails = Action.async {implicit request =>
    try{
      val oauthCode: String = request.getQueryString("code").get
      
      for{
        accessToken <- facebook.getOAuthAccessToken(oauthCode, userDetailsURL)
        userDetails <- facebook.getUserDetails(accessToken)
//        postMessageStatistic <- facebook.getPostMessage(accessToken)
      } yield{
        Found(testUserDetailsURL).withSession(request.session + ("UserDetails" -> userDetails))
      }
    } catch {
      case e: Exception => Future {InternalServerError(e.getMessage)}
      
    }
  }
  
  //Method to get post statistic from FB
  def testPostDetails = Action {implicit request=>
    if (request.session.get("PostDetails").isEmpty){
      TemporaryRedirect(postDetailsAuthURL)
    } else {
      Ok(s"Post Details: \r\n ${request.session("PostDetails")}")
    }
  }
  
  def postDetailsAuth = Action { implicit request =>
    Found(facebook.getOAuthAuthorizationURL(postDetailsURL))
  }
  
  def getPostDetails = Action.async {implicit request =>
    try{
      val oauthCode: String = request.getQueryString("code").get

      for{
        accessToken <- facebook.getOAuthAccessToken(oauthCode, postDetailsURL)
//        userDetails <- facebook.getUserDetails(accessToken)
        postMessageStatistic <- facebook.getPostMessage(accessToken)
      } yield{
        Found(testPostDetailsURL).withSession(request.session + ("PostDetails" -> postMessageStatistic))
      }
    } catch {
      case e: Exception => Future {InternalServerError(e.getMessage)}
    }
  }
}