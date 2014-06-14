package com.socialorra.api.registry

import com.socialorra.api.repo.email.EmailRepositoryComponent
import com.socialorra.api.service.EmailServiceComponent

/**
 * Registry for all email related components
 */
object EmailComponentRegistry extends
EmailServiceComponent with
EmailRepositoryComponent
{
  val emailRepository = EmailRepositoryImpl
  val emailService = new EmailServiceImpl
}