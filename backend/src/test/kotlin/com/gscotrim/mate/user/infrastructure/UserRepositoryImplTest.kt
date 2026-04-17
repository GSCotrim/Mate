package com.gscotrim.mate.user.infrastructure

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Instant
import java.util.UUID

class UserRepositoryImplTest {

    private val userJpaRepository = mock(UserJpaRepository::class.java)
    private val repository = UserRepositoryImpl(userJpaRepository)

    private val userId = UUID.randomUUID()
    private val email = "giovanni@mate.com"
    private val userJpa = UserJpa(
        id = userId,
        email = email,
        passwordHash = "hashed_password",
        name = "Giovanni",
        createdAt = Instant.now()
    )

    @Test
    fun `getUserCredentialsBy returns UserCredentials when found`() {
        `when`(userJpaRepository.findUserByEmail(email)).thenReturn(userJpa)

        val credentials = repository.getUserCredentialsBy(email)

        assertNotNull(credentials)
        assertEquals(userId, credentials!!.id)
        assertEquals(email, credentials.email)
        assertEquals("hashed_password", credentials.passwordHash)
    }

    @Test
    fun `getUserCredentialsBy returns null when not found`() {
        `when`(userJpaRepository.findUserByEmail(email)).thenReturn(null)

        assertNull(repository.getUserCredentialsBy(email))
    }
}