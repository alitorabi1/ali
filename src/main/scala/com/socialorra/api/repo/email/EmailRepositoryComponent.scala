package com.socialorra.api.repo.email

import akka.actor.Props
import akka.actor.ActorSystem
import com.typesafe.config.{ConfigException, ConfigFactory}

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
  object EmailRepositoryImpl extends EmailRepository {

    val conf = ConfigFactory.load()

    val smtpHost = try { conf.getString("smtp.host") } catch { case e: ConfigException.Missing => "localhost" }
    val smtpPort = try { conf.getInt("smtp.port")    } catch { case e: ConfigException.Missing => 25 }
    val smtpSsl  = try { conf.getBoolean("smtp.ssl") } catch { case e: ConfigException.Missing => false }
    val smtpTls  = try { conf.getBoolean("smtp.tls") } catch { case e: ConfigException.Missing => false }

    val smtpUser     = try { Some(conf.getString("smtp.user"))     } catch { case e: ConfigException.Missing => None }
    val smtpPassword = try { Some(conf.getString("smtp.password")) } catch { case e: ConfigException.Missing => None }

    val smtpNumSenders = try { conf.getInt("smtp.number-of-senders") } catch { case e: ConfigException.Missing => 1 }

    // TODO: move this into an object of it's own
    val senderProps = Props(classOf[SmtpActor],
      smtpHost,
      smtpPort,
      smtpSsl,
      smtpTls,
      smtpUser,
      smtpPassword)

    val actorSystem = ActorSystem("so-api")
    val sender = actorSystem.actorOf(senderProps, "smtp-actor")

    def send(email: String, subject: String, message: String) {
      sender ! EmailMessage(
        from       = EmailAddress("todo@change.me", ""),
        subject    = subject,
        textBody   = None,
        htmlBody   = Some(message),
        recipients = List(EmailAddress(email, "")))
    }
  }

  /**
   * Email repository that delivers email to standard out
   */
  object StdoutEmailRepositoryImpl extends EmailRepository {
    def send(email: String, subject: String, message: String) {
      println(s"${getClass.getSimpleName}: $email -> $subject -> $message")
    }
  }
}

