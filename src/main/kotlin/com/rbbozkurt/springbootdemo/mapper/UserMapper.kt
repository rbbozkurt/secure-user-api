package com.rbbozkurt.springbootdemo.mapper

import com.rbbozkurt.springbootdemo.dto.UserCreateRequestDto
import com.rbbozkurt.springbootdemo.dto.UserDto
import com.rbbozkurt.springbootdemo.dto.UserUpdateRequestDto
import com.rbbozkurt.springbootdemo.persistence.entity.RoleEntity
import com.rbbozkurt.springbootdemo.persistence.entity.UserEntity
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    uses = [RoleMapper::class]
)
interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = ["roleNamesToSet"])
    fun toDto(user: UserEntity): UserDto

    @Named("roleNamesToSet")
    fun roleNamesToSet(roles: Set<RoleEntity>): Set<String> {
        return roles.map { it.name }.toSet()
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastPasswordUpdate", ignore = true)
    @Mapping(target = "roles", source = "roles")
    fun toEntity(dto: UserCreateRequestDto, roles: Set<RoleEntity>): UserEntity

    @Mapping(target = "id", source = "existingUser.id")
    @Mapping(target = "username", source = "updateDto.username", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", source = "updateDto.password", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", source = "updateDto.email", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdAt", source = "existingUser.createdAt")
    @Mapping(target = "lastPasswordUpdate", expression = "java(updateDto.getPassword() != null ? java.time.LocalDateTime.now() : existingUser.getLastPasswordUpdate())")
    @Mapping(target = "roles", source = "roles", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun mergeToEntity(existingUser: UserEntity, updateDto: UserUpdateRequestDto, roles: Set<RoleEntity>): UserEntity

    // Fixed annotation - using Kotlin syntax
    @IterableMapping(elementTargetType = UserDto::class)
    fun toDtoList(entities: List<UserEntity>): List<UserDto>
}