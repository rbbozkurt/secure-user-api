package com.rbbozkurt.springbootdemo.service.impl

import com.rbbozkurt.springbootdemo.dto.RoleDto
import com.rbbozkurt.springbootdemo.exception.ResourceNotFoundException
import com.rbbozkurt.springbootdemo.mapper.RoleMapper
import com.rbbozkurt.springbootdemo.persistence.entity.RoleEntity
import com.rbbozkurt.springbootdemo.persistence.repository.RoleRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.*
import java.util.Optional

class RoleServiceImplTest {

    private val roleRepository: RoleRepository = mock()
    private val roleMapper: RoleMapper = mock()
    private val roleService = RoleServiceImpl(roleRepository, roleMapper)

    private val testRoleEntity = RoleEntity(1L, "ADMIN")
    private val testRoleDto = RoleDto(1L, "ADMIN")

    @Test
    fun `getAllRoles should return list of RoleDto`() {
        whenever(roleRepository.findAll()).thenReturn(listOf(testRoleEntity))
        whenever(roleMapper.toDto(testRoleEntity)).thenReturn(testRoleDto)

        val result = roleService.getAllRoles()

        assertEquals(1, result.size)
        assertEquals("ADMIN", result[0].name)
    }

    @Test
    fun `getRoleById should return RoleDto if found`() {
        whenever(roleRepository.findById(1L)).thenReturn(Optional.of(testRoleEntity))
        whenever(roleMapper.toDto(testRoleEntity)).thenReturn(testRoleDto)

        val result = roleService.getRoleById(1L)

        assertEquals("ADMIN", result.name)
    }

    @Test
    fun `getRoleById should throw if not found`() {
        whenever(roleRepository.findById(2L)).thenReturn(Optional.empty())

        val exception = assertThrows(ResourceNotFoundException::class.java) {
            roleService.getRoleById(2L)
        }
        assertEquals("Role with ID 2 not found", exception.message)
    }

    @Test
    fun `createRole should save and return RoleDto`() {
        whenever(roleMapper.toEntity(testRoleDto)).thenReturn(testRoleEntity)
        whenever(roleRepository.save(testRoleEntity)).thenReturn(testRoleEntity)
        whenever(roleMapper.toDto(testRoleEntity)).thenReturn(testRoleDto)

        val result = roleService.createRole(testRoleDto)

        assertEquals("ADMIN", result.name)
    }

    @Test
    fun `deleteRole should delete if exists`() {
        whenever(roleRepository.existsById(1L)).thenReturn(true)

        roleService.deleteRole(1L)

        verify(roleRepository).deleteById(1L)
    }

    @Test
    fun `deleteRole should throw if not exists`() {
        whenever(roleRepository.existsById(2L)).thenReturn(false)

        val exception = assertThrows(ResourceNotFoundException::class.java) {
            roleService.deleteRole(2L)
        }
        assertEquals("Role with ID 2 not found", exception.message)
    }

    @Test
    fun `updateRole should update name and return updated RoleDto`() {
        val updatedDto = RoleDto(1L, "MODERATOR")
        val updatedEntity = RoleEntity(1L, "MODERATOR")

        whenever(roleRepository.findById(1L)).thenReturn(Optional.of(testRoleEntity))
        whenever(roleRepository.save(any())).thenReturn(updatedEntity)
        whenever(roleMapper.toDto(updatedEntity)).thenReturn(updatedDto)

        val result = roleService.updateRole(1L, updatedDto)

        assertEquals("MODERATOR", result.name)
    }
}
