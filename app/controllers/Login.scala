package controllers

import play.api.mvc._

object Login extends Controller {

  val userService = UserComponentRegistry.userService

  def auth = Action { implicit request => {
        for {
          params <- request.body.asFormUrlEncoded
          email <- params.getOrElse("email", Seq.empty[String]).headOption
          password <- params.getOrElse("password", Seq.empty[String]).headOption
          authenticated <- Some(userService.authenticate(email, password))
          if (authenticated)
        } yield {

          val r = params.getOrElse("next", Seq.empty[String]).headOption match {
            case Some(redirect) => Found(redirect)
            case None => NoContent
          }

          r.withSession("email" -> email)

        }
      } getOrElse {
    Unauthorized
  }}
}


// This is a user DAO
trait UserRepositoryComponent {
  val userRepository: UserRepository

  // User DAO's respect this trait
  trait UserRepository {
    def authenticate(email: String, password: String): Boolean
  }

  // A fake user DAO
  class FakeUserRepository extends UserRepository {
    val PASS = "password"
    def authenticate(email: String, password: String): Boolean = password == PASS
  }
}

// This is a user service / API that needs a user DAO
trait UserServiceComponent {
  this: UserRepositoryComponent =>

  // This is injected
  val userService: UserService

  // User services respect this trait
  trait UserService {
    def authenticate(email: String, password: String): Boolean
  }

  // A fake user service
  class FakeUserService extends UserService {
    def authenticate(email: String, password: String): Boolean = userRepository.authenticate(email, password)
  }
}

// Registry for all user related components
object UserComponentRegistry extends
UserServiceComponent with
UserRepositoryComponent
{
  val userRepository = new FakeUserRepository
  val userService = new FakeUserService
}

