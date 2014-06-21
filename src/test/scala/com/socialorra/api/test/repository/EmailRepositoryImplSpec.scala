package com.socialorra.api.test.repository

import com.icegreen.greenmail.util.{GreenMailUtil, GreenMail}
import com.socialorra.api.registry.EmailComponentRegistry
import com.socialorra.api.test.UnitSpec
import org.scalatest.BeforeAndAfterAll

class EmailRepositoryImplSpec extends UnitSpec with BeforeAndAfterAll {

  val greenMail = new GreenMail()

  override def beforeAll() {
    greenMail.start()
  }

  override def afterAll() {
    greenMail.stop()
  }

  "A real EmailRepository" should "deliver real email" in {
    val repo = EmailComponentRegistry.emailRepository
    repo.send("hisham.mardambey@gmail.com", "test subject", "test message")

    assert(greenMail.waitForIncomingEmail(100, 1) == true)

    val messages = greenMail.getReceivedMessages()

    assert(messages.length == 1)

    val message = messages(0)

    assert(message.getAllRecipients.length >= 1)
    assert(message.getAllRecipients.head.toString == "hisham.mardambey@gmail.com")
    assert(message.getSubject == "test subject")
    assert(GreenMailUtil.getBody(message).trim.contains("test message"))
  }
}
