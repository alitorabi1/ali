package com.socialorra.api.service

import com.socialorra.api.repo.twitter.{AccessToken, RequestToken, TwitterRepositoryComponent}

/**
 * Twitter services.
 */
trait TwitterServiceComponent {
  this: TwitterRepositoryComponent =>

  /**
   * TwitterService to use, injected via registry
   */
  val twitterService: TwitterService

  // Twitter services implement this trait
  trait TwitterService {
    def getOAuthAccessToken(requestToken: RequestToken, oauthVerifier: String) : AccessToken
    def getOAuthRequestToken(callbackURL: String) : RequestToken
    def getScreenName(implicit accessToken: AccessToken) : String
  }

  // Twitter service that communicates with Twitter via injected twitter repository
  class TwitterServiceImpl extends TwitterService {
    def getOAuthAccessToken(requestToken: RequestToken, oauthVerifier: String) : AccessToken = {
      twitterRepository.getOAuthAccessToken(requestToken, oauthVerifier)
    }

    def getOAuthRequestToken(callbackURL: String) : RequestToken = {
      twitterRepository.getOAuthRequestToken(callbackURL)
    }

    def getScreenName(implicit accessToken: AccessToken) : String = {
      twitterRepository.getScreenName(accessToken)
    }
  }
}

