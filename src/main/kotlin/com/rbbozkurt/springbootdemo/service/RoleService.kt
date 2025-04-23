package com.rbbozkurt.springbootdemo.service

import com.rbbozkurt.springbootdemo.dto.RoleDto

interface RoleService {
    fun getAllRoles(): List<RoleDto>
    fun getRoleById(id: Long): RoleDto
    fun createRole(dto: RoleDto): RoleDto
    fun deleteRole(id: Long)
    fun updateRole(id: Long, updatedDto: RoleDto): RoleDto

}
