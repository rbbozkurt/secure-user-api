package com.rbbozkurt.springbootdemo.config

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Configuration
@ConfigurationProperties(prefix = "admin.default")
@Validated
class AdminProperties {

    @NotBlank(message = "Admin username must not be blank")
    lateinit var username: String

    @NotBlank(message = "Admin password must not be blank")
    lateinit var password: String

    @NotBlank(message = "Admin email must not be blank")
    lateinit var email: String

    @NotBlank(message = "Admin role must not be blank")
    lateinit var role: String
}
