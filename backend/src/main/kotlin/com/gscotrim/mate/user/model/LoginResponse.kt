package com.gscotrim.mate.user.model

import com.gscotrim.mate.shared.security.JwtToken

data class LoginResponse (
    val token: JwtToken
)