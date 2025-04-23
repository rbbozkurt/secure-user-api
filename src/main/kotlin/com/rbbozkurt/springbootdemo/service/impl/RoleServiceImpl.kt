package com.rbbozkurt.springbootdemo.service.impl

import com.rbbozkurt.springbootdemo.dto.RoleDto
import com.rbbozkurt.springbootdemo.mapper.RoleMapper
import com.rbbozkurt.springbootdemo.persistence.repository.RoleRepository
import com.rbbozkurt.springbootdemo.service.RoleService
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository,
    private val roleMapper: RoleMapper
) : RoleService {

    override fun getAllRoles(): List<RoleDto> {
        return roleRepository.findAll().map(roleMapper::toDto)
    }

    override fun getRoleById(id: Long): RoleDto {
        val role = roleRepository.findById(id)
            .orElseThrow { NoSuchElementException("Role with ID $id not found") }
        return roleMapper.toDto(role)
    }

    override fun createRole(dto: RoleDto): RoleDto {
        val entity = roleMapper.toEntity(dto)
        val saved = roleRepository.save(entity)
        return roleMapper.toDto(saved)
    }

    override fun deleteRole(id: Long) {
        if (!roleRepository.existsById(id)) {
            throw NoSuchElementException("Role with ID $id not found")
        }
        roleRepository.deleteById(id)
    }

    override fun updateRole(id: Long, updatedDto: RoleDto): RoleDto {
        val role = roleRepository.findById(id)
            .orElseThrow { NoSuchElementException("Role with ID $id not found") }

        val updatedEntity = role.copy(name = updatedDto.name) // safer
        val saved = roleRepository.save(updatedEntity)
        return roleMapper.toDto(saved)
    }


}
