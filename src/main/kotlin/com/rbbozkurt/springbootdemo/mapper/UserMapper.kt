package com.rbbozkurt.springbootdemo.mapper

import com.rbbozkurt.springbootdemo.dto.*
import com.rbbozkurt.springbootdemo.persistence.entity.UserEntity
import org.mapstruct.*
import java.util.*

@Mapper(componentModel = "spring", uses = [RoleMapper::class])
interface UserMapper {

    fun toDto(entity: UserEntity): UserDto

    fun toEntity(dto: UserCreateRequestDto): UserEntity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateEntityFromDto(dto: UserUpdateRequestDto, @MappingTarget entity: UserEntity)
}
