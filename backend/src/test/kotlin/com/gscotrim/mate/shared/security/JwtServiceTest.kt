package com.gscotrim.mate.shared.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
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
    fun `returns null for a token signed with a different key`() {
        val otherService = JwtService("other-secret-key-that-is-at-least-32-bytes!!", expiration)
        val token = otherService.generateToken(UUID.randomUUID())
        assertNull(jwtService.extractUserId(token))
    }

    @Test
    fun `returns null for a tampered token`() {
        val token = jwtService.generateToken(UUID.randomUUID())
        val tampered = token.dropLast(5) + "XXXXX"
        assertNull(jwtService.extractUserId(tampered))
    }

    @Test
    fun `returns null for an expired token`() {
        val expiredToken = JwtService(secret, expiration = -1L).generateToken(UUID.randomUUID())
        assertNull(jwtService.extractUserId(expiredToken))
    }
}
