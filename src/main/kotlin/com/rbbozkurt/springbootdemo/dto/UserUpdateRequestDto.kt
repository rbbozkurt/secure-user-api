package com.rbbozkurt.springbootdemo.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UserUpdateRequestDto(
    val username: String? = null,

    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String? = null,

    @field:Email(message = "Email should be valid")
    val email: String? = null,

    val roleIds: Set<Long>? = null

)
