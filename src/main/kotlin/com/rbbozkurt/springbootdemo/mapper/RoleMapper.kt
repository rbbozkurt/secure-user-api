package com.rbbozkurt.springbootdemo.mapper

import com.rbbozkurt.springbootdemo.dto.RoleDto
import com.rbbozkurt.springbootdemo.persistence.entity.RoleEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface RoleMapper {
    fun toDto(role: RoleEntity): RoleDto
    fun toEntity(roleDto: RoleDto): RoleEntity
}
