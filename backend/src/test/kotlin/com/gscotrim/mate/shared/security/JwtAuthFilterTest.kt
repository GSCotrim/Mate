package com.gscotrim.mate.shared.security

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

class JwtAuthFilterTest {

    private val secret = "test-secret-key-that-is-at-least-32-bytes-long!!"
    private val jwtService = JwtService(secret, expiration = 3600L)
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

        filter.doFilter(request, MockHttpServletResponse(), MockFilterChain())

        assertEquals(userId, SecurityContextHolder.getContext().authentication?.principal)
    }

    @Test
    fun `skips authentication when Authorization header is missing`() {
        filter.doFilter(MockHttpServletRequest(), MockHttpServletResponse(), MockFilterChain())

        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `skips authentication when token is invalid`() {
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer invalid.token.here")

        filter.doFilter(request, MockHttpServletResponse(), MockFilterChain())

        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `does not overwrite existing authentication`() {
        val firstUserId = UUID.randomUUID()
        val secondUserId = UUID.randomUUID()

        val firstRequest = MockHttpServletRequest()
        firstRequest.addHeader("Authorization", "Bearer ${jwtService.generateToken(firstUserId)}")
        filter.doFilter(firstRequest, MockHttpServletResponse(), MockFilterChain())

        val secondRequest = MockHttpServletRequest()
        secondRequest.addHeader("Authorization", "Bearer ${jwtService.generateToken(secondUserId)}")
        filter.doFilter(secondRequest, MockHttpServletResponse(), MockFilterChain())

        assertEquals(firstUserId, SecurityContextHolder.getContext().authentication?.principal)
    }
}