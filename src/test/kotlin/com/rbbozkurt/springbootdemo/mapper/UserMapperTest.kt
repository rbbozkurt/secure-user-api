package com.rbbozkurt.springbootdemo.mapper

import com.rbbozkurt.springbootdemo.dto.UserCreateRequestDto
import com.rbbozkurt.springbootdemo.persistence.entity.RoleEntity
import com.rbbozkurt.springbootdemo.persistence.entity.UserEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

class UserMapperTest {

    private val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)

    @Test
    fun `should map UserEntity to UserDto`() {
        val role = RoleEntity(id = 1, name = "ADMIN")
        val entity = UserEntity(
            id = 1,
            username = "john",
            password = "encoded",
            email = "john@example.com",
            createdAt = LocalDateTime.now(),
            lastPasswordUpdate = LocalDateTime.now(),
            roles = mutableSetOf(role)
        )

        val dto = userMapper.toDto(entity)

        assertEquals("john", dto.username)
        assertEquals("john@example.com", dto.email)
        assertTrue(dto.roles.contains("ADMIN"))
    }

    @Test
    fun `should map UserCreateRequestDto to UserEntity with roles`() {
        val request = UserCreateRequestDto(
            username = "jane",
            password = "securepass",
            email = "jane@example.com",
            roleIds = setOf(1)
        )

        val roles = setOf(RoleEntity(id = 1, name = "USER"))

        val entity = userMapper.toEntity(request, roles)

        assertEquals("jane", entity.username)
        assertEquals("jane@example.com", entity.email)
        assertTrue(entity.roles.any { it.name == "USER" })
    }

    @Test
    fun `should map list of entities to list of DTOs`() {
        val role = RoleEntity(id = 1, name = "USER")
        val user1 = UserEntity(
            id = 1,
            username = "u1",
            password = "pass",
            email = "u1@example.com",
            createdAt = LocalDateTime.now(),
            lastPasswordUpdate = LocalDateTime.now(),
            roles = mutableSetOf(role)
        )
        val user2 = user1.copy(id = 2, username = "u2", email = "u2@example.com")

        val result = userMapper.toDtoList(listOf(user1, user2))

        assertEquals(2, result.size)
        assertEquals("u1", result[0].username)
        assertEquals("u2", result[1].username)
    }
}
