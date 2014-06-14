package com.socialorra.api.repo.email

import akka.actor.Actor
import org.apache.commons.mail._

case class EmailHeader(name: String, value: String)
case class EmailAddress(address: String, name: String)
case class EmailMessage (
  from: EmailAddress,  textBody: Option[String],
  subject: String,
  htmlBody: Option[String],
  recipients: List[EmailAddress],
  replyTo: Option[EmailAddress] = None,
  headers: Option[List[EmailHeader]] = None)

class SmtpActor(
  smtpHost: String,
  smtpPort: Int,
  smtpSsl: Boolean,
  smtpTls: Boolean,
  smtpUser: Option[String],
  smtpPass: Option[String])
  extends Actor {

  def receive = {

    case email: EmailMessage => {
      val e = new HtmlEmail()

      e.setCharset(("utf-8"))
      e.setSubject(email.subject)
      e.setFrom(email.from.address, email.from.name)

      email.textBody.map(e.setTextMsg)
      email.htmlBody.map(e.setHtmlMsg)
      email.replyTo.map(replyTo => e.addReplyTo(replyTo.address, replyTo.name))
      email.recipients.foreach(recipient => e.addTo(recipient.address, recipient.name))
      email.headers.map(_.foreach(header => e.addHeader(header.name, header.value)))

      e.setHostName(smtpHost)
      e.setSmtpPort(smtpPort)
      e.setSSLOnConnect(smtpSsl)
      e.setStartTLSEnabled(smtpTls)
      for(u <- smtpUser; p <- smtpPass) yield e.setAuthenticator(new DefaultAuthenticator(u, p))
      e.setDebug(false)
      e.send
    }
  }

}
