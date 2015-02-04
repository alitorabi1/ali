package com.socialorra.api.repo.email

import akka.actor.Props

import akka.actor.ActorSystem
import com.typesafe.config.{ ConfigException, ConfigFactory }

/** Email repositories must implement this trait
 */
trait EmailRepository {
  def send(email: String, subject: String, message: String)
}

/** Email repository that communicates with the real world
 */
object EmailRepositoryImpl extends EmailRepository {

  val conf = ConfigFactory.load()

  val smtpHost = try { conf.getString("so-api.smtp.host") } catch { case e: ConfigException.Missing ⇒ "localhost" }
  val smtpPort = try { conf.getInt("so-api.smtp.port") } catch { case e: ConfigException.Missing ⇒ 25 }
  val smtpSsl = try { conf.getBoolean("so-api.smtp.ssl") } catch { case e: ConfigException.Missing ⇒ false }
  val smtpTls = try { conf.getBoolean("so-api.smtp.tls") } catch { case e: ConfigException.Missing ⇒ false }

  val smtpUser = try { Some(conf.getString("so-api.smtp.user")) } catch { case e: ConfigException.Missing ⇒ None }
  val smtpPassword = try { Some(conf.getString("so-api.smtp.password")) } catch { case e: ConfigException.Missing ⇒ None }

  val smtpNumSenders = try { conf.getInt("so-api.smtp.number-of-senders") } catch { case e: ConfigException.Missing ⇒ 1 }

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
      from = EmailAddress("todo@change.me", ""),
      subject = subject,
      textBody = None,
      htmlBody = Some(message),
      recipients = List(EmailAddress(email, "")))
  }
}

/** Email repository that delivers email to standard out
 */
object StdoutEmailRepositoryImpl extends EmailRepository {
  def send(email: String, subject: String, message: String) {
    println(s"${getClass.getSimpleName}: $email -> $subject -> $message")
  }
}

