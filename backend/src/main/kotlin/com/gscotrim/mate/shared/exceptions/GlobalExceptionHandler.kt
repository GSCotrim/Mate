package com.gscotrim.mate.shared.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleInvalidCredentials(ex: InvalidCredentialsException): ErrorResponse {
        return ErrorResponse(ex.message ?: "Invalid credentials")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ErrorResponse {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ErrorResponse(message)
    }
}