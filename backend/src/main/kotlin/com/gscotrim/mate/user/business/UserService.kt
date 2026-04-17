package com.gscotrim.mate.user.business

import com.gscotrim.mate.shared.exceptions.InvalidCredentialsException
import com.gscotrim.mate.shared.security.JwtService
import com.gscotrim.mate.shared.security.JwtToken
import com.gscotrim.mate.user.model.LoginRequest
import com.gscotrim.mate.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {

    fun loginUser(loginRequest: LoginRequest): JwtToken {
        val credentials = userRepository.getUserCredentialsBy(loginRequest.email)
            ?: throw InvalidCredentialsException()
        if (!passwordEncoder.matches(loginRequest.password, credentials.passwordHash)) {
            throw InvalidCredentialsException()
        }
        return jwtService.generateToken(credentials.id)
    }
}
