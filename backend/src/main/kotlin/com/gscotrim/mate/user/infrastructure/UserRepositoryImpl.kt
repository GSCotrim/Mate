package com.gscotrim.mate.user.infrastructure

import com.gscotrim.mate.user.model.UserCredentials
import com.gscotrim.mate.user.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    override fun getUserCredentialsBy(email: String): UserCredentials? {
        return userJpaRepository.findUserByEmail(email)?.toUserCredentials()
    }
}