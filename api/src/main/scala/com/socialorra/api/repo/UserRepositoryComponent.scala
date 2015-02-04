package com.socialorra.api.repo

// User repositories respect this trait
trait UserRepository {
  def authenticate(email: String, password: String): Boolean
}

// A fake user repository
class FakeUserRepository extends UserRepository {
  val PASS = "password"
  def authenticate(email: String, password: String): Boolean = password == PASS
}

