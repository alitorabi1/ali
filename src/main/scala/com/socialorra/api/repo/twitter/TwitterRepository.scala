package com.socialorra.api.repo.twitter

import twitter4j.TwitterFactory
import twitter4j.auth.{AccessToken => TAccessToken}
import twitter4j.auth.{RequestToken => TRequestToken}

/**
 * Twitter repositories must implement this trait
 */
trait TwitterRepository {
  def getOAuthAccessToken(requestToken: RequestToken, oauthVerifier: String) : AccessToken
  def getOAuthRequestToken(callbackURL: String) : RequestToken
  def getScreenName(implicit accessToken: AccessToken) : String
}

/**
 * Twitter repository that communicates with the real world
 */
class TwitterRepositoryImpl extends TwitterRepository {

  def getOAuthAccessToken(requestToken: RequestToken, oauthVerifier: String) : AccessToken = {
    val twitter = new TwitterFactory().getInstance()
    val frequestToken = new TRequestToken(requestToken.token, requestToken.secret)
    val faccessToken = twitter.getOAuthAccessToken(frequestToken, oauthVerifier)
    AccessToken(faccessToken.getToken, faccessToken.getTokenSecret, faccessToken.getUserId)
  }

  def getOAuthRequestToken(callbackURL: String) : RequestToken = {
    val twitter = new TwitterFactory().getInstance()
    val requestToken = twitter.getOAuthRequestToken(callbackURL) // TODO: catch TwitterException
    RequestToken(requestToken.getToken, requestToken.getTokenSecret)
  }

  def getScreenName(implicit accessToken: AccessToken) : String = {
    val twitter = new TwitterFactory().getInstance()
    val faccessToken = new TAccessToken(accessToken.token, accessToken.secret)
    twitter.setOAuthAccessToken(faccessToken)
    twitter.getScreenName
  }
}

