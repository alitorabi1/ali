package com.socialorra.api.registry

import com.socialorra.api.repo.facebook.{FacebookRepository, FacebookRepositoryImpl}

/**
 * Registry for all facebook related components
 */
object FacebookRegistry {
  val facebookRepository: FacebookRepository = FacebookRepositoryImpl
}