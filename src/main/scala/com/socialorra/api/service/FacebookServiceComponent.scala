package com.socialorra.api.service

import com.socialorra.api.repo.facebook.{AccessToken, FacebookRepositoryComponent}

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
    def getOAuthAccessToken(oauthCode: String, callbackURL: String) : AccessToken
    def getName(implicit accessToken: AccessToken) : String
  }

  // Facebook service that sends facebooks using injected facebook repository
  class FacebookServiceImpl extends FacebookService {
    def getOAuthAuthorizationURL(callbackURL: String) : String = {
      facebookRepository.getOAuthAuthorizationURL(callbackURL)
    }

    def getOAuthAccessToken(oauthCode: String, callbackURL: String) : AccessToken = {
      facebookRepository.getOAuthAccessToken(oauthCode, callbackURL)
    }

    def getName(implicit accessToken: AccessToken) : String = {
      facebookRepository.getName(accessToken)
    }
  }
}

