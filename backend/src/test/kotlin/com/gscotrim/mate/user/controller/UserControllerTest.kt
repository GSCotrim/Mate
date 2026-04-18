package com.gscotrim.mate.user.controller

import com.gscotrim.mate.shared.config.SecurityConfig
import com.gscotrim.mate.shared.exceptions.InvalidCredentialsException
import com.gscotrim.mate.shared.security.JwtService
import com.gscotrim.mate.shared.security.JwtToken
import com.gscotrim.mate.user.business.UserService
import com.gscotrim.mate.user.model.LoginRequest
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(UserController::class)
@Import(SecurityConfig::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var userService: UserService

    @MockitoBean
    private lateinit var jwtService: JwtService

    @Test
    fun `login returns 200 with token for valid credentials`() {
        `when`(userService.loginUser(LoginRequest("user@example.com", "password123")))
            .thenReturn(JwtToken("mocked.jwt.token"))

        mockMvc.post("/api/v1/users/login") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"email": "user@example.com", "password": "password123"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { value("mocked.jwt.token") }
        }
    }

    @Test
    fun `login returns 400 when request body has blank fields`() {
        mockMvc.post("/api/v1/users/login") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"email": "", "password": ""}"""
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `login returns 400 when email is malformed`() {
        mockMvc.post("/api/v1/users/login") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"email": "notanemail", "password": "password123"}"""
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `login returns 401 when credentials are invalid`() {
        `when`(userService.loginUser(LoginRequest("user@example.com", "wrongpassword")))
            .thenThrow(InvalidCredentialsException())

        mockMvc.post("/api/v1/users/login") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"email": "user@example.com", "password": "wrongpassword"}"""
        }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.message") { value("Invalid email or password.") }
        }
    }
}