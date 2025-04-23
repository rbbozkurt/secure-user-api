package com.rbbozkurt.springbootdemo.service.impl

import com.rbbozkurt.springbootdemo.dto.UserCreateRequestDto
import com.rbbozkurt.springbootdemo.dto.UserDto
import com.rbbozkurt.springbootdemo.dto.UserUpdateRequestDto
import com.rbbozkurt.springbootdemo.exception.ResourceNotFoundException
import com.rbbozkurt.springbootdemo.exception.UsernameAlreadyExistsException
import com.rbbozkurt.springbootdemo.mapper.UserMapper
import com.rbbozkurt.springbootdemo.persistence.repository.RoleRepository
import com.rbbozkurt.springbootdemo.persistence.repository.UserRepository
import com.rbbozkurt.springbootdemo.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun listUsers(): List<UserDto> {
        val users = userRepository.findAll()
        return userMapper.toDtoList(users)
    }

    override fun getUserById(id: Long): UserDto {
        val user = userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("User not found with id: $id")
        }
        return userMapper.toDto(user)
    }

    @Transactional
    override fun createUser(request: UserCreateRequestDto): UserDto {
        if (userRepository.existsByUsername(request.username)) {
            throw UsernameAlreadyExistsException("Username already exists: ${request.username}")
        }

        // Fetch roles
        val roles = request.roleIds.mapNotNull { roleId ->
            roleRepository.findById(roleId).orElse(null)
        }.toSet()

        // Encode password
        val requestWithEncodedPassword = request.copy(password = passwordEncoder.encode(request.password))

        // Create and save user entity
        val userEntity = userMapper.toEntity(requestWithEncodedPassword, roles)
        val savedUser = userRepository.save(userEntity)
        return userMapper.toDto(savedUser)
    }

    @Transactional
    override fun updateUser(id: Long, request: UserUpdateRequestDto): UserDto {
        val existingUser = userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("User not found with id: $id")
        }

        // Check username uniqueness if trying to update username
        if (request.username != null && request.username != existingUser.username) {
            if (userRepository.existsByUsername(request.username)) {
                throw UsernameAlreadyExistsException("Username already exists: ${request.username}")
            }
        }

        // Encode password if present
        val requestWithEncodedPassword = if (request.password != null) {
            request.copy(password = passwordEncoder.encode(request.password))
        } else {
            request
        }

        // Get roles if roleIds are provided
        val roles = if (request.roleIds != null) {
            request.roleIds.mapNotNull { roleId ->
                roleRepository.findById(roleId).orElse(null)
            }.toSet()
        } else {
            null
        }

        // Create a new entity with updated values
        val updatedUser = userRepository.save(
            userMapper.mergeToEntity(existingUser, requestWithEncodedPassword, roles ?: existingUser.roles)
        )

        return userMapper.toDto(updatedUser)
    }

    @Transactional
    override fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException("User not found with id: $id")
        }
        userRepository.deleteById(id)
    }
}