package com.socialorra.api.registry

import com.socialorra.api.service.FacebookServiceComponent
import com.socialorra.api.repo.facebook.FacebookRepositoryComponent

/**
 * Registry for all facebook related components
 */
object FacebookComponentRegistry extends
FacebookServiceComponent with
FacebookRepositoryComponent
{
  val facebookRepository = new FacebookRepositoryImpl
  val facebookService = new FacebookServiceImpl
}