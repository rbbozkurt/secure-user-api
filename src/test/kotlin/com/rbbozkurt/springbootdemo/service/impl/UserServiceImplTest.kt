package com.rbbozkurt.springbootdemo.service.impl

import com.rbbozkurt.springbootdemo.dto.*
import com.rbbozkurt.springbootdemo.exception.ResourceNotFoundException
import com.rbbozkurt.springbootdemo.exception.UsernameAlreadyExistsException
import com.rbbozkurt.springbootdemo.mapper.UserMapper
import com.rbbozkurt.springbootdemo.persistence.entity.RoleEntity
import com.rbbozkurt.springbootdemo.persistence.entity.UserEntity
import com.rbbozkurt.springbootdemo.persistence.repository.RoleRepository
import com.rbbozkurt.springbootdemo.persistence.repository.UserRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.*
import org.mockito.kotlin.any
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class UserServiceImplTest {

    private val userRepository: UserRepository = mock()
    private val roleRepository: RoleRepository = mock()
    private val userMapper: UserMapper = mock()
    private val passwordEncoder: PasswordEncoder = mock()

    private val service = UserServiceImpl(userRepository, roleRepository, userMapper, passwordEncoder)

    private val userEntity = UserEntity(
        id = 1L,
        username = "john",
        password = "encoded",
        email = "john@example.com"
    )

    private val userDto = UserDto(
        id = 1L,
        username = "john",
        email = "john@example.com",
        createdAt = userEntity.createdAt,
        lastPasswordUpdate = userEntity.lastPasswordUpdate,
        roles = setOf("USER")
    )

    @Test
    fun `should list all users`() {
        whenever(userRepository.findAll()).thenReturn(listOf(userEntity))
        whenever(userMapper.toDtoList(any())).thenReturn(listOf(userDto))

        val result = service.listUsers()

        assertEquals(1, result.size)
        assertEquals("john", result[0].username)
    }

    @Test
    fun `should get user by id`() {
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(userEntity))
        whenever(userMapper.toDto(userEntity)).thenReturn(userDto)

        val result = service.getUserById(1L)

        assertEquals("john", result.username)
    }

    @Test
    fun `should throw exception when user not found`() {
        whenever(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.getUserById(1L)
        }
    }

    @Test
    fun `should create user`() {
        val request = UserCreateRequestDto("jane", "pass123", "jane@example.com", setOf(1L))
        val encodedPassword = "hashed_pass"
        val role = RoleEntity(id = 1, name = "USER")
        val requestWithEncodedPassword = request.copy(password = encodedPassword)

        whenever(userRepository.existsByUsername("jane")).thenReturn(false)
        whenever(roleRepository.findById(1L)).thenReturn(Optional.of(role))
        whenever(passwordEncoder.encode("pass123")).thenReturn(encodedPassword)
        whenever(userMapper.toEntity(eq(requestWithEncodedPassword), any())).thenReturn(userEntity)
        whenever(userRepository.save(userEntity)).thenReturn(userEntity)
        whenever(userMapper.toDto(userEntity)).thenReturn(userDto)

        val result = service.createUser(request)

        assertEquals("john", result.username)
    }

    @Test
    fun `should not create user with existing username`() {
        val request = UserCreateRequestDto("john", "pass", "test@example.com", setOf(1L))
        whenever(userRepository.existsByUsername("john")).thenReturn(true)

        assertThrows<UsernameAlreadyExistsException> {
            service.createUser(request)
        }
    }

    @Test
    fun `should update user`() {
        val updateRequest = UserUpdateRequestDto("johnny", null, "johnny@example.com", null)
        val updatedEntity = userEntity.copy(username = "johnny", email = "johnny@example.com")
        val updatedDto = userDto.copy(username = "johnny", email = "johnny@example.com")

        whenever(userRepository.findById(1L)).thenReturn(Optional.of(userEntity))
        whenever(userRepository.existsByUsername("johnny")).thenReturn(false)
        whenever(userMapper.mergeToEntity(userEntity, updateRequest, userEntity.roles)).thenReturn(updatedEntity)
        whenever(userRepository.save(updatedEntity)).thenReturn(updatedEntity)
        whenever(userMapper.toDto(updatedEntity)).thenReturn(updatedDto)

        val result = service.updateUser(1L, updateRequest)

        assertEquals("johnny", result.username)
        assertEquals("johnny@example.com", result.email)
    }


    @Test
    fun `should delete user`() {
        whenever(userRepository.existsById(1L)).thenReturn(true)

        assertDoesNotThrow {
            service.deleteUser(1L)
        }

        verify(userRepository).deleteById(1L)
    }

    @Test
    fun `should throw when deleting non-existent user`() {
        whenever(userRepository.existsById(1L)).thenReturn(false)

        assertThrows<ResourceNotFoundException> {
            service.deleteUser(1L)
        }
    }
}
