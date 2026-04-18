package com.gscotrim.mate.user.model

import java.util.UUID

data class User (
    val id: UUID,
    val email: String,
    val name: String,
)
