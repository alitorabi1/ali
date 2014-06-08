package com.socialorra.api.registry

import com.socialorra.api.service.EmailServiceComponent
import com.socialorra.api.repo.EmailRepositoryComponent

/**
 * Registry for all email related components
 */
object EmailComponentRegistry extends
EmailServiceComponent with
EmailRepositoryComponent
{
  val emailRepository = new EmailRepositoryImpl
  val emailService = new EmailServiceImpl
}