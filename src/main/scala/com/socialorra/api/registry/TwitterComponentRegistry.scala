package com.socialorra.api.registry

import com.socialorra.api.service.TwitterServiceComponent
import com.socialorra.api.repo.twitter.TwitterRepositoryComponent

/**
 * Registry for all twitter related components
 */
object TwitterComponentRegistry extends
TwitterServiceComponent with
TwitterRepositoryComponent
{
  val twitterRepository = new TwitterRepositoryImpl
  val twitterService = new TwitterServiceImpl
}