package com.gscotrim.mate.user.infrastructure

import com.gscotrim.mate.user.model.User
import com.gscotrim.mate.user.model.UserCredentials

fun UserJpa.toUser() =
    User (
        id = id,
        email = email,
        name = name
    )

fun UserJpa.toUserCredentials() =
    UserCredentials (
        id = id,
        email = email,
        passwordHash = passwordHash
    )