package com.rbbozkurt.springbootdemo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception thrown when a requested resource is not found.
 * Maps to HTTP 404 NOT FOUND response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String) : RuntimeException(message)

/**
 * Exception thrown when attempting to create a user with a username that already exists.
 * Maps to HTTP 409 CONFLICT response.
 */
@ResponseStatus(HttpStatus.CONFLICT)
class UsernameAlreadyExistsException(message: String) : RuntimeException(message)