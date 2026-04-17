package com.gscotrim.mate.user.infrastructure

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
class UserJpa (
    @Id
    @Column
    val id: UUID,
    @Column(unique = true, nullable = false)
    val email: String,
    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,
    @Column(nullable = false)
    val name: String,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant
)
