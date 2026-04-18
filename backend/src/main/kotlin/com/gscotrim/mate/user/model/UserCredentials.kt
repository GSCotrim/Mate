package com.gscotrim.mate.user.model

import java.util.UUID

data class UserCredentials (
    val id: UUID,
    val email: String,
    val passwordHash: String,
)
