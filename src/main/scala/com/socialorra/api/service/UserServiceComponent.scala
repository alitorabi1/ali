package com.socialorra.api.service

import com.socialorra.api.repo.UserRepositoryComponent

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

