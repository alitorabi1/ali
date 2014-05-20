package test

import play.api.test.{FakeHeaders, FakeRequest, WithApplication, PlaySpecification}
import play.api.mvc.AnyContentAsEmpty

object LoginControllerSpec extends PlaySpecification {

  val EMAIL = "test@example.com"
  val PASS  = "password"

  "successfully log in" in new WithApplication {

    val result = controllers.Login.auth()(
      FakeRequest("POST", "/")
      .withFormUrlEncodedBody("email" -> EMAIL, "password" -> PASS)
    )

    status (result)              must equalTo(NO_CONTENT)
    session(result) get("email") must equalTo(Some(EMAIL))
  }

  "not be able to log in" in new WithApplication {

    val result = controllers.Login.auth()(
      FakeRequest("POST", "/")
      .withFormUrlEncodedBody("email" -> EMAIL, "password" -> "WRONGPASS")
    )

    status (result)              must equalTo(UNAUTHORIZED)
    session(result) get("email") must equalTo(None)
  }
}
