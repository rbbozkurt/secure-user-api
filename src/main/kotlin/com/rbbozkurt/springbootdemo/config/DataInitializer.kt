package com.rbbozkurt.springbootdemo.config

import com.rbbozkurt.springbootdemo.persistence.entity.RoleEntity
import com.rbbozkurt.springbootdemo.persistence.entity.UserEntity
import com.rbbozkurt.springbootdemo.persistence.repository.RoleRepository
import com.rbbozkurt.springbootdemo.persistence.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val adminProperties: AdminProperties
) : ApplicationRunner {

    private val logger = LoggerFactory.getLogger(DataInitializer::class.java)

    override fun run(args: ApplicationArguments?) {
        if (userRepository.count() == 0L) {
            val adminRole = roleRepository.findByName(adminProperties.role)
                ?: roleRepository.save(RoleEntity(name = adminProperties.role))

            val adminUser = UserEntity(
                username = adminProperties.username,
                password = passwordEncoder.encode(adminProperties.password),
                email = adminProperties.email,
                roles = mutableSetOf(adminRole),
                createdAt = LocalDateTime.now(),
                lastPasswordUpdate = LocalDateTime.now()
            )

            userRepository.save(adminUser)
            logger.info("Default admin user created: ${adminProperties.username}")
        }
    }
}
