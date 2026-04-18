package com.gscotrim.mate.user.repository

import com.gscotrim.mate.user.model.UserCredentials

interface UserRepository {

    fun getUserCredentialsBy(email: String): UserCredentials?

}