package com.gscotrim.mate.user.controller

import com.gscotrim.mate.user.business.UserService
import com.gscotrim.mate.user.model.LoginRequest
import com.gscotrim.mate.user.model.LoginResponse

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun loginUser(@Valid @RequestBody loginRequest: LoginRequest): LoginResponse {
        return LoginResponse(userService.loginUser(loginRequest))
    }
}
