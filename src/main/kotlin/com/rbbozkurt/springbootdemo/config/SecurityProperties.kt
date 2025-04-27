package com.rbbozkurt.springbootdemo.config

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.security.user")
@Validated // ➔ enables automatic validation at startup
class SecurityProperties {

    @NotBlank(message = "Username must not be blank")
    lateinit var name: String

    @NotBlank(message = "Password must not be blank")
    lateinit var password: String
}
