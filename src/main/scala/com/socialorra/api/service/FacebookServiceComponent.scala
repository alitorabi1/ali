package com.socialorra.api.service

import com.socialorra.api.repo.facebook.{AccessToken, FacebookRepositoryComponent}
import scala.concurrent._
import ExecutionContext.Implicits.global

/**
 * Facebook services.
 */
trait FacebookServiceComponent {
  this: FacebookRepositoryComponent =>

  /**
   * FacebookService to use, injected via registry
   */
  val facebookService: FacebookService

  // Facebook services implement this trait
  trait FacebookService {
    def getOAuthAuthorizationURL(callbackURL: String) : String
    def getOAuthAccessToken(oauthCode: String, callbackURL: String) : Future[AccessToken]
    def getName(implicit accessToken: AccessToken) : Future[String]
  }

  // Facebook service that sends facebooks using injected facebook repository
  class FacebookServiceImpl extends FacebookService {
    def getOAuthAuthorizationURL(callbackURL: String) : String = {
      facebookRepository.getOAuthAuthorizationURL(callbackURL)
    }

    def getOAuthAccessToken(oauthCode: String, callbackURL: String) : Future[AccessToken] = future {
      facebookRepository.getOAuthAccessToken(oauthCode, callbackURL)
    }

    def getName(implicit accessToken: AccessToken) : Future[String] = future {
      facebookRepository.getName(accessToken)
    }
  }
}

