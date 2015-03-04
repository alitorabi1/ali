package com.socialorra.api.repo.facebook

import facebook4j._
import facebook4j.auth.{ AccessToken ⇒ FAccessToken }

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.math.Ordering

/** Facebook repositories must implement this trait
 */
trait FacebookRepository {
  def getOAuthAuthorizationURL(callbackURL: String): String
  def getOAuthAccessToken(oauthCode: String, callbackURL: String): Future[AccessToken]
  def getName(implicit accessToken: AccessToken): Future[String]
  def postMessages(implicit accessToken: AccessToken): Future[String]
  def getPostMessage(implicit accessToken: AccessToken): Future[String]
  def getUserDetails(implicit accessToken: AccessToken): Future[String]

}

/** Facebook repository that communicates with the real world
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

  def postMessages(implicit accessToken: AccessToken): Future[String] = Future {
    val facebook = new FacebookFactory().getInstance()
    val faccessToken = new FAccessToken(accessToken.token, accessToken.expires)
    facebook.setOAuthAccessToken(faccessToken)
    val post = new PostUpdate("This is a test message - 24")
    facebook.postFeed(post)
  }

  def getPostMessage(implicit accessToken: AccessToken): Future[String] = Future {
    val facebook = new FacebookFactory().getInstance()
    val faccessToken = new FAccessToken(accessToken.token, accessToken.expires)
    facebook.setOAuthAccessToken(faccessToken)
    val pm = facebook.getPost("997374436958813_1006397072723216")

    val postedMessage: String = "\nPosted Message: " + pm.getMessage
    val postedTime: String = "\nPost Created Time: " + pm.getCreatedTime.toString
    val postedLikeCount: String = "\nTotal number of likes: " + pm.getLikes.size().toString
    val postedCommentCount: String = "\nTotal number of comments: " + pm.getComments.size().toString
    //    val shareCount = { if (pm.getSharesCount > 0) pm.getSharesCount else 0 }
    //    val postedShareCount: String = "\nTotal number of shares: " + 0

    val postLike = pm.getLikes
    var likeNames: String = ""
    if (postLike.size() > 0) {
      for (i ← 0 to postLike.size() - 1) {
        likeNames = likeNames + s"\nLike $i Name: " + postLike.get(i).getName
      }
    }
    val postComment = pm.getComments
    var comments: String = ""
    if (postComment.size() > 0) {
      for (i ← 0 to postComment.size() - 1) {
        comments = comments + s"\r\n Comment $i from: " + postComment.get(i).getFrom.getName + " Comment is: " + postComment.get(i).getMessage
      }
    }

    val postMessageStatistic: String = "Here is the statistic about your post: \r\n" + postedMessage + postedTime +
      postedLikeCount + postedCommentCount + /*postedShareCount +*/ "\nList of likes: \r\n" + likeNames + "\nList of comments: \r\n" + comments
    postMessageStatistic
  }

  def getUserDetails(implicit accessToken: AccessToken): Future[String] = Future {
    val facebook = new FacebookFactory().getInstance()
    val faccessToken = new FAccessToken(accessToken.token, accessToken.expires)
    facebook.setOAuthAccessToken(faccessToken)

    val userAlbums = facebook.getAlbums()
    var albumsName: String = ""
    if (userAlbums.size() > 0) {
      for (i ← 0 to userAlbums.size() - 1) {
        albumsName = albumsName + s" \r\n Album: $i Name: " + (userAlbums.get(i).getName)
      }
    }

    val userEvents = facebook.getEvents
    var eventsName: String = ""
    if (userEvents.size() > 0) {
      for (i ← 0 to userEvents.size() - 1) {
        eventsName = eventsName + s"\r\n Event: $i Name: " + userEvents.get(i).getName
      }
    }

    val userDetails: String = "UserName: " + facebook.getName + "\r\n" + "User Id: " + facebook.getId + "\r\n" +
      "Link: " + facebook.getLink(facebook.getId).getLink + "\r\n" + "List of Albums: " + albumsName + eventsName

    userDetails
  }
}

