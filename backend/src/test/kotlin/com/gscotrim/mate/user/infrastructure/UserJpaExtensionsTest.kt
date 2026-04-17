package com.gscotrim.mate.user.infrastructure

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.UUID

class UserJpaExtensionsTest {

    private val userId = UUID.randomUUID()
    private val userJpa = UserJpa(
        id = userId,
        email = "giovanni@mate.com",
        passwordHash = "hashed_password",
        name = "Giovanni",
        createdAt = Instant.now()
    )

    @Test
    fun `toUser maps id, email and name`() {
        val user = userJpa.toUser()

        assertEquals(userId, user.id)
        assertEquals("giovanni@mate.com", user.email)
        assertEquals("Giovanni", user.name)
    }

    @Test
    fun `toUserCredentials maps id, email and passwordHash`() {
        val credentials = userJpa.toUserCredentials()

        assertEquals(userId, credentials.id)
        assertEquals("giovanni@mate.com", credentials.email)
        assertEquals("hashed_password", credentials.passwordHash)
    }
}