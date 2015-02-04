package com.socialorra.api.registry

import com.socialorra.api.repo.email.{EmailRepositoryImpl, EmailRepository}

/**
 * Registry for all email related components
 */
object EmailRegistry extends {
  val emailRepository: EmailRepository = EmailRepositoryImpl
}