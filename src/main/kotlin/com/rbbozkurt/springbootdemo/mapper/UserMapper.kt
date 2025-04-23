package com.rbbozkurt.springbootdemo.mapper

import com.rbbozkurt.springbootdemo.dto.*
import com.rbbozkurt.springbootdemo.persistence.entity.RoleEntity
import com.rbbozkurt.springbootdemo.persistence.entity.UserEntity
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRolesToStrings(entity.getRoles()))")
    fun toDto(entity: UserEntity): UserDto

    fun toEntity(dto: UserCreateRequestDto): UserEntity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateEntityFromDto(dto: UserUpdateRequestDto, @MappingTarget entity: UserEntity)

    // Helper method for custom mapping
    fun mapRolesToStrings(roles: Set<RoleEntity>): Set<String> {
        return roles.map { it.name }.toSet()
    }
}
