package com.rbbozkurt.springbootdemo.service

import com.rbbozkurt.springbootdemo.dto.UserCreateRequestDto
import com.rbbozkurt.springbootdemo.dto.UserDto
import com.rbbozkurt.springbootdemo.dto.UserUpdateRequestDto

interface UserService {
    fun createUser(request: UserCreateRequestDto): UserDto
    fun updateUser(id: Long, request: UserUpdateRequestDto): UserDto
    fun getUserById(id: Long): UserDto
    fun deleteUser(id: Long)
    fun listUsers(): List<UserDto>
}
