package com.gscotrim.mate.user.business

import com.gscotrim.mate.shared.exceptions.InvalidCredentialsException
import com.gscotrim.mate.shared.security.JwtService
import com.gscotrim.mate.shared.security.JwtToken
import com.gscotrim.mate.user.model.LoginRequest
import com.gscotrim.mate.user.model.UserCredentials
import com.gscotrim.mate.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

class UserServiceTest {

    private val userRepository = mock(UserRepository::class.java)
    private val jwtService = mock(JwtService::class.java)
    private val passwordEncoder = mock(PasswordEncoder::class.java)
    private val userService = UserService(userRepository, jwtService, passwordEncoder)

    private val userId = UUID.randomUUID()
    private val email = "giovanni@mate.com"
    private val rawPassword = "password123"
    private val hashedPassword = "hashed_password"
    private val credentials = UserCredentials(userId, email, hashedPassword)
    private val expectedToken = JwtToken("mocked.jwt.token")

    @Test
    fun `loginUser returns a token for the authenticated user when credentials are valid`() {
        `when`(userRepository.getUserCredentialsBy(email)).thenReturn(credentials)
        `when`(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true)
        `when`(jwtService.generateToken(userId)).thenReturn(expectedToken)

        val token = userService.loginUser(LoginRequest(email, rawPassword))

        assertEquals(expectedToken, token)
        verify(jwtService).generateToken(userId)
    }

    @Test
    fun `loginUser throws InvalidCredentialsException when user does not exist`() {
        `when`(userRepository.getUserCredentialsBy(email)).thenReturn(null)

        assertThrows<InvalidCredentialsException> {
            userService.loginUser(LoginRequest(email, rawPassword))
        }
    }

    @Test
    fun `loginUser throws InvalidCredentialsException when password is wrong`() {
        `when`(userRepository.getUserCredentialsBy(email)).thenReturn(credentials)
        `when`(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false)

        assertThrows<InvalidCredentialsException> {
            userService.loginUser(LoginRequest(email, rawPassword))
        }
    }

    @Test
    fun `loginUser does not reveal whether email or password was wrong`() {
        `when`(userRepository.getUserCredentialsBy(email)).thenReturn(null)
        val noUserException = assertThrows<InvalidCredentialsException> {
            userService.loginUser(LoginRequest(email, rawPassword))
        }

        `when`(userRepository.getUserCredentialsBy(email)).thenReturn(credentials)
        `when`(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false)
        val badPasswordException = assertThrows<InvalidCredentialsException> {
            userService.loginUser(LoginRequest(email, rawPassword))
        }

        assertEquals(noUserException.message, badPasswordException.message)
    }
}