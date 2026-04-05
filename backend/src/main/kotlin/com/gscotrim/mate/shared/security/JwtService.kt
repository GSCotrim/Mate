package com.gscotrim.mate.shared.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @param:Value("\${jwt.secret}")
    private val secret: String,
    @param:Value("\${jwt.expiration}")
    private val expiration: Long
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(userId: UUID): String = Jwts.builder()
        .subject(userId.toString())
        .issuedAt(Date())
        .expiration(Date(System.currentTimeMillis() + expiration * 1000))
        .signWith(key)
        .compact()

    fun extractUserId(token: String): UUID =
        UUID.fromString(
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
        )

    fun isValid(token: String): Boolean = runCatching {
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
    }.isSuccess
}
