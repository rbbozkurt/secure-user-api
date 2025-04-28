package com.rbbozkurt.springbootdemo.service.auth

import com.rbbozkurt.springbootdemo.persistence.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class ApplicationUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        val authorities = userEntity.roles.map { role ->
            SimpleGrantedAuthority("ROLE_${role.name}")
        }.toList()

        return User(
            userEntity.username,
            userEntity.password,
            authorities
        )
    }
}
