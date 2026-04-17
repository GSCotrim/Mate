package com.gscotrim.mate.shared.security

import jakarta.servlet.FilterChain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

class JwtAuthFilterTest {

    private val secret = "test-secret-key-that-is-at-least-32-bytes-long!!"
    private val jwtService = JwtService(secret, expirationSeconds = 3600L)
    private val filter = JwtAuthFilter(jwtService)

    @AfterEach
    fun clearContext() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `sets authentication for a valid token`() {
        val userId = UUID.randomUUID()
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer ${jwtService.generateToken(userId)}")

        var capturedPrincipal: Any? = null
        filter.doFilter(request, MockHttpServletResponse(), FilterChain { _, _ ->
            capturedPrincipal = SecurityContextHolder.getContext().authentication?.principal
        })

        assertEquals(userId, capturedPrincipal)
    }

    @Test
    fun `skips authentication when Authorization header is missing`() {
        var capturedAuthentication: Any? = "sentinel"
        filter.doFilter(MockHttpServletRequest(), MockHttpServletResponse(), FilterChain { _, _ ->
            capturedAuthentication = SecurityContextHolder.getContext().authentication
        })

        assertNull(capturedAuthentication)
    }

    @Test
    fun `skips authentication when token is invalid`() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer invalid.token.here")

        var capturedAuthentication: Any? = "sentinel"
        filter.doFilter(request, MockHttpServletResponse(), FilterChain { _, _ ->
            capturedAuthentication = SecurityContextHolder.getContext().authentication
        })

        assertNull(capturedAuthentication)
    }

    @Test
    fun `does not overwrite existing authentication`() {
        val existingUserId = UUID.randomUUID()
        val incomingUserId = UUID.randomUUID()

        val existingContext = SecurityContextHolder.createEmptyContext()
        existingContext.authentication = UsernamePasswordAuthenticationToken(existingUserId, null, emptyList())
        SecurityContextHolder.setContext(existingContext)

        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer ${jwtService.generateToken(incomingUserId)}")

        var capturedPrincipal: Any? = null
        filter.doFilter(request, MockHttpServletResponse(), FilterChain { _, _ ->
            capturedPrincipal = SecurityContextHolder.getContext().authentication?.principal
        })

        assertEquals(existingUserId, capturedPrincipal)
    }
}