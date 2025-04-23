package com.rbbozkurt.springbootdemo.dto

import java.time.LocalDateTime

data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
    val createdAt: LocalDateTime,
    val lastPasswordUpdate: LocalDateTime,
    val roles: Set<String>
)
