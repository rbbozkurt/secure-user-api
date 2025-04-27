package com.rbbozkurt.springbootdemo.controller

import com.rbbozkurt.springbootdemo.dto.UserCreateRequestDto
import com.rbbozkurt.springbootdemo.dto.UserDto
import com.rbbozkurt.springbootdemo.dto.UserUpdateRequestDto
import com.rbbozkurt.springbootdemo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun listUsers(): List<UserDto> = userService.listUsers()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): UserDto = userService.getUserById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@Valid @RequestBody request: UserCreateRequestDto): UserDto = userService.createUser(request)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @Valid @RequestBody request: UserUpdateRequestDto): UserDto =
        userService.updateUser(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }
}
