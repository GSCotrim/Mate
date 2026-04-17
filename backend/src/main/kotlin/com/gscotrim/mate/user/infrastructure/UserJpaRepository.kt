package com.gscotrim.mate.user.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserJpaRepository : JpaRepository<UserJpa, UUID> {

    fun findUserByEmail(email: String): UserJpa?

}