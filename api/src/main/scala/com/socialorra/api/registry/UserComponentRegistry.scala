package com.socialorra.api.registry

import com.socialorra.api.repo.FakeUserRepository

// Registry for all user related components
object UserComponentRegistry {
  val userRepository = new FakeUserRepository
}