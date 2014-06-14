package com.socialorra.api.service

import com.socialorra.api.repo.email.EmailRepositoryComponent

/**
 * Email services.
 */
trait EmailServiceComponent {
  this: EmailRepositoryComponent =>

  /**
   * EmailService to use, injected via registry
   */
  val emailService: EmailService

  // Email services implement this trait
  trait EmailService {
    def send(email: String, subject: String, message: String)
  }

  // Email service that sends emails using injected email repository
  class EmailServiceImpl extends EmailService {
    def send(email: String, subject: String, message: String) {
      emailRepository.send(email, subject, message)
    }
  }
}

