package com.rbbozkurt.springbootdemo.service.impl

import com.rbbozkurt.springbootdemo.dto.RoleDto
import com.rbbozkurt.springbootdemo.mapper.RoleMapper
import com.rbbozkurt.springbootdemo.persistence.entity.RoleEntity
import com.rbbozkurt.springbootdemo.persistence.repository.RoleRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

class RoleServiceImplTest {

    @Mock
    private lateinit var roleRepository: RoleRepository

    @Mock
    private lateinit var roleMapper: RoleMapper

    @InjectMocks
    private lateinit var roleService: RoleServiceImpl

    private val testRoleEntity = RoleEntity(1L, "ADMIN")
    private val testRoleDto = RoleDto(1L, "ADMIN")

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        roleService = RoleServiceImpl(roleRepository, roleMapper)
    }

    @Test
    fun `getAllRoles should return list of RoleDto`() {
        `when`(roleRepository.findAll()).thenReturn(listOf(testRoleEntity))
        `when`(roleMapper.toDto(testRoleEntity)).thenReturn(testRoleDto)

        val result = roleService.getAllRoles()

        assertEquals(1, result.size)
        assertEquals("ADMIN", result[0].name)
    }

    @Test
    fun `getRoleById should return RoleDto if found`() {
        `when`(roleRepository.findById(1L)).thenReturn(Optional.of(testRoleEntity))
        `when`(roleMapper.toDto(testRoleEntity)).thenReturn(testRoleDto)

        val result = roleService.getRoleById(1L)

        assertEquals("ADMIN", result.name)
    }

    @Test
    fun `getRoleById should throw if not found`() {
        `when`(roleRepository.findById(2L)).thenReturn(Optional.empty())

        val exception = assertThrows(NoSuchElementException::class.java) {
            roleService.getRoleById(2L)
        }
        assertEquals("Role with ID 2 not found", exception.message)
    }

    @Test
    fun `createRole should save and return RoleDto`() {
        `when`(roleMapper.toEntity(testRoleDto)).thenReturn(testRoleEntity)
        `when`(roleRepository.save(testRoleEntity)).thenReturn(testRoleEntity)
        `when`(roleMapper.toDto(testRoleEntity)).thenReturn(testRoleDto)

        val result = roleService.createRole(testRoleDto)

        assertEquals("ADMIN", result.name)
    }

    @Test
    fun `deleteRole should delete if exists`() {
        `when`(roleRepository.existsById(1L)).thenReturn(true)

        roleService.deleteRole(1L)

        verify(roleRepository, times(1)).deleteById(1L)
    }

    @Test
    fun `deleteRole should throw if not exists`() {
        `when`(roleRepository.existsById(2L)).thenReturn(false)

        val exception = assertThrows(NoSuchElementException::class.java) {
            roleService.deleteRole(2L)
        }
        assertEquals("Role with ID 2 not found", exception.message)
    }

    @Test
    fun `updateRole should update name and return updated RoleDto`() {
        val updatedDto = RoleDto(1L, "MODERATOR")
        val updatedEntity = RoleEntity(1L, "MODERATOR")

        `when`(roleRepository.findById(1L)).thenReturn(Optional.of(testRoleEntity))
        `when`(roleRepository.save(any(RoleEntity::class.java))).thenReturn(updatedEntity)
        `when`(roleMapper.toDto(updatedEntity)).thenReturn(updatedDto)

        val result = roleService.updateRole(1L, updatedDto)

        assertEquals("MODERATOR", result.name)
    }
}