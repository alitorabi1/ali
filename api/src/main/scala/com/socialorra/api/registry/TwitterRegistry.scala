package com.socialorra.api.registry

import com.socialorra.api.repo.twitter.{ TwitterRepository, TwitterRepositoryImpl }

/** Registry for all twitter related components
 */
object TwitterRegistry {
  val twitterRepository: TwitterRepository = new TwitterRepositoryImpl
}