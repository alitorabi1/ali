package com.socialorra.api.repo.facebook

import facebook4j.Facebook
import facebook4j.{ PostUpdate, FacebookFactory, ResponseList, User }
import facebook4j.auth.{ AccessToken ⇒ FAccessToken }

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/** Facebook repositories must implement this trait
 */
trait FacebookRepository {
  def getOAuthAuthorizationURL(callbackURL: String): String
  def getOAuthAccessToken(oauthCode: String, callbackURL: String): Future[AccessToken]
  def getName(implicit accessToken: AccessToken): Future[String]
  def getEmail(implicit accessToken: AccessToken): Future[String]
}

/** Facebook repository that communicates with the real world
 *
 */
object FacebookRepositoryImpl extends FacebookRepository {
  def getOAuthAuthorizationURL(callbackURL: String): String = {
    val facebook = new FacebookFactory().getInstance()
    facebook.getOAuthAuthorizationURL(callbackURL)
  }

  def getOAuthAccessToken(oauthCode: String, callbackURL: String): Future[AccessToken] = Future {
    val facebook = new FacebookFactory().getInstance()
    val accessToken = facebook.getOAuthAccessToken(oauthCode, callbackURL)
    AccessToken(accessToken.getToken, accessToken.getExpires)
  }

  def getName(implicit accessToken: AccessToken): Future[String] = Future {
    val facebook = new FacebookFactory().getInstance()
    val faccessToken = new FAccessToken(accessToken.token, accessToken.expires)
    facebook.setOAuthAccessToken(faccessToken)
    facebook.getName
  }

  def getEmail(implicit accessToken: AccessToken): Future[String] = Future {
    val facebook = new FacebookFactory().getInstance()
    val faccessToken = new FAccessToken(accessToken.token, accessToken.expires)
    facebook.setOAuthAccessToken(faccessToken)
    facebook.getMe.getEmail
  }

}

