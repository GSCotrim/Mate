package com.gscotrim.mate.shared.exceptions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException

class GlobalExceptionHandlerTest {

    private val handler = GlobalExceptionHandler()

    @Test
    fun `handleInvalidCredentials returns ErrorResponse with exception message`() {
        val ex = InvalidCredentialsException()
        val response = handler.handleInvalidCredentials(ex)

        assertEquals("Invalid email or password.", response.message)
    }

    @Test
    fun `handleValidationErrors returns ErrorResponse with field error messages`() {
        val bindingResult = mock(BindingResult::class.java)
        `when`(bindingResult.fieldErrors).thenReturn(listOf(
            FieldError("loginRequest", "email", "must not be blank")
        ))
        val ex = mock(MethodArgumentNotValidException::class.java)
        `when`(ex.bindingResult).thenReturn(bindingResult)

        val response = handler.handleValidationErrors(ex)

        assertEquals("email: must not be blank", response.message)
    }
}