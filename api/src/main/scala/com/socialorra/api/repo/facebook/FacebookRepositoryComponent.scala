package com.socialorra.api.repo.facebook

import facebook4j.FacebookFactory
import facebook4j.auth.{AccessToken => FAccessToken}

/**
 * Facebook repositories must implement this trait
 */
trait FacebookRepository {
  def getOAuthAuthorizationURL(callbackURL: String) : String
  def getOAuthAccessToken(oauthCode: String, callbackURL: String) : AccessToken
  def getName(implicit accessToken: AccessToken) : String
}

/**
 * Facebook repository that communicates with the real world
 */
object FacebookRepositoryImpl extends FacebookRepository {
  def getOAuthAuthorizationURL(callbackURL: String) : String = {
    val facebook = new FacebookFactory().getInstance()
    facebook.getOAuthAuthorizationURL(callbackURL)
  }

  def getOAuthAccessToken(oauthCode: String, callbackURL: String) : AccessToken = {
    val facebook = new FacebookFactory().getInstance()
    val accessToken = facebook.getOAuthAccessToken(oauthCode, callbackURL)
    AccessToken(accessToken.getToken, accessToken.getExpires)
  }

  def getName(implicit accessToken: AccessToken) : String = {
    val facebook = new FacebookFactory().getInstance()
    val faccessToken = new FAccessToken(accessToken.token, accessToken.expires)
    facebook.setOAuthAccessToken(faccessToken)
    facebook.getName
  }
}

