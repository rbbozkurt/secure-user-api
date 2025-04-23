package com.rbbozkurt.springbootdemo.mapper

import com.rbbozkurt.springbootdemo.dto.*
import com.rbbozkurt.springbootdemo.persistence.entity.RoleEntity
import com.rbbozkurt.springbootdemo.persistence.entity.UserEntity
import org.mapstruct.*

@Mapper(componentModel = "spring")
abstract class UserMapper {

    @Mapping(target = "roles", expression = "java(mapRolesToStrings(entity.getRoles()))")
    abstract fun toDto(entity: UserEntity): UserDto

    abstract fun toEntity(dto: UserCreateRequestDto): UserEntity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun updateEntityFromDto(dto: UserUpdateRequestDto, @MappingTarget entity: UserEntity)

    protected fun mapRolesToStrings(roles: Set<RoleEntity>): Set<String> {
        return roles.map { it.name }.toSet()
    }
}

