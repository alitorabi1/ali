package com.socialorra.api.repo

/**
 * Email repositories
 */
trait EmailRepositoryComponent {

  /**
   * EmailRepository to use, injected via registry
   */
  val emailRepository: EmailRepository

  /**
   * Email repositories must implement this trait
   */
  trait EmailRepository {
    def send(email: String, subject: String, message: String)
  }

  /**
   * Email repository that communicates with the real world
   */
  class EmailRepositoryImpl extends EmailRepository {
    def send(email: String, subject: String, message: String) {
      // TODO: send email
    }
  }

  /**
   * Email repository that delivers email to standard out
   */
  class StdoutEmailRepositoryImpl extends EmailRepository {
    def send(email: String, subject: String, message: String) {
      println(s"${getClass.getSimpleName}: $email -> $subject -> $message")
    }
  }
}

