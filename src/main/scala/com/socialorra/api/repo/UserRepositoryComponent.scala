package com.socialorra.api.repo

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

