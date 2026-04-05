package com.gscotrim.mate.shared.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class JwtServiceTest {

    private val secret = "test-secret-key-that-is-at-least-32-bytes-long!!"
    private val expiration = 3600L
    private val jwtService = JwtService(secret, expiration)

    @Test
    fun `generates a token and extracts the subject`() {
        val userId = UUID.randomUUID()
        val token = jwtService.generateToken(userId)
        assertEquals(userId, jwtService.extractUserId(token))
    }

    @Test
    fun `validates a valid token`() {
        val token = jwtService.generateToken(UUID.randomUUID())
        assertTrue(jwtService.isValid(token))
    }

    @Test
    fun `rejects a tampered token`() {
        val token = jwtService.generateToken(UUID.randomUUID())
        val tampered = token.dropLast(5) + "XXXXX"
        assertFalse(jwtService.isValid(tampered))
    }

    @Test
    fun `rejects an expired token`() {
        val expiredService = JwtService(secret, expiration = -1L)
        val token = expiredService.generateToken(UUID.randomUUID())
        assertFalse(jwtService.isValid(token))
    }
}