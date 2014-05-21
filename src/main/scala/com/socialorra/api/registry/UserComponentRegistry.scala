package com.socialorra.api.registry

import com.socialorra.api.service.UserServiceComponent
import com.socialorra.api.repo.UserRepositoryComponent

// Registry for all user related components
object UserComponentRegistry extends
UserServiceComponent with
UserRepositoryComponent
{
  val userRepository = new FakeUserRepository
  val userService = new FakeUserService
}