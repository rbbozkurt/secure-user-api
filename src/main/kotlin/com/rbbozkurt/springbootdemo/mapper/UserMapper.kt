package com.rbbozkurt.springbootdemo.mapper

import com.rbbozkurt.springbootdemo.dto.*
import com.rbbozkurt.springbootdemo.persistence.entity.RoleEntity
import com.rbbozkurt.springbootdemo.persistence.entity.UserEntity
import org.mapstruct.*
import java.time.LocalDateTime

@Mapper(componentModel = "spring")
abstract class UserMapper {

    fun toDto(user: UserEntity): UserDto {
        return UserDto(
            id = user.id,
            username = user.username,
            email = user.email,
            createdAt = user.createdAt,
            lastPasswordUpdate = user.lastPasswordUpdate,
            roles = user.roles.map { it.name }.toSet()
        )
    }

    fun toEntity(dto: UserCreateRequestDto, roles: Set<RoleEntity>): UserEntity {
        return UserEntity(
            username = dto.username,
            password = dto.password,
            email = dto.email,
            roles = roles.toMutableSet()
        )
    }

    fun mergeToEntity(existingUser: UserEntity, updateDto: UserUpdateRequestDto, roles: Set<RoleEntity>): UserEntity {
        updateDto.username?.let { existingUser.username = it }
        updateDto.password?.let {
            existingUser.password = it
            existingUser.lastPasswordUpdate = LocalDateTime.now()
        }
        updateDto.email?.let { existingUser.email = it }
        if (roles.isNotEmpty()) {
            existingUser.roles = roles.toMutableSet()
        }
        return existingUser
    }

    abstract fun toDtoList(entities: List<UserEntity>): List<UserDto>
}
