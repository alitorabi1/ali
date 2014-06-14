package com.socialorra.api.service

import scala.concurrent._
import ExecutionContext.Implicits.global
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
    def getOAuthAccessToken(requestToken: RequestToken, oauthVerifier: String) : Future[AccessToken]
    def getOAuthRequestToken(callbackURL: String) : Future[RequestToken]
    def getScreenName(implicit accessToken: AccessToken) : Future[String]
  }

  // Twitter service that communicates with Twitter via injected twitter repository
  class TwitterServiceImpl extends TwitterService {
    def getOAuthAccessToken(requestToken: RequestToken, oauthVerifier: String) : Future[AccessToken] = future {
      twitterRepository.getOAuthAccessToken(requestToken, oauthVerifier)
    }

    def getOAuthRequestToken(callbackURL: String) : Future[RequestToken] = future {
      twitterRepository.getOAuthRequestToken(callbackURL)
    }

    def getScreenName(implicit accessToken: AccessToken) : Future[String] = future {
      twitterRepository.getScreenName(accessToken)
    }
  }
}

