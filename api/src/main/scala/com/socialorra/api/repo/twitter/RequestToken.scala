package com.socialorra.api.repo.twitter

import twitter4j.auth.{RequestToken => TRequestToken}

case class RequestToken(token: String, secret: String) {
   val requestToken = new TRequestToken(token, secret)

   def getAuthenticationURL : String = requestToken.getAuthenticationURL
 }
