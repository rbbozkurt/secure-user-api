package com.rbbozkurt.springbootdemo.controller

import com.rbbozkurt.springbootdemo.dto.RoleDto
import com.rbbozkurt.springbootdemo.service.RoleService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/roles")
class RoleController(
    private val roleService: RoleService
) {

    @GetMapping
    fun listRoles(): List<RoleDto> = roleService.getAllRoles()

    @GetMapping("/{id}")
    fun getRoleById(@PathVariable id: Long): RoleDto = roleService.getRoleById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createRole(@Valid @RequestBody roleDto: RoleDto): RoleDto = roleService.createRole(roleDto)

    @PutMapping("/{id}")
    fun updateRole(@PathVariable id: Long, @Valid @RequestBody updatedRoleDto: RoleDto): RoleDto =
        roleService.updateRole(id, updatedRoleDto)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRole(@PathVariable id: Long) {
        roleService.deleteRole(id)
    }
}
