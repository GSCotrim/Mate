package com.gscotrim.mate.shared.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
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
    private val logger = LoggerFactory.getLogger(JwtService::class.java)
    private val key = Keys.hmacShaKeyFor(secret.trim().toByteArray())

    fun generateToken(userId: UUID): String = Jwts.builder()
        .subject(userId.toString())
        .issuedAt(Date())
        .expiration(Date(System.currentTimeMillis() + expiration * 1000))
        .signWith(key)
        .compact()

    fun extractUserId(token: String): UUID? =
        try {
            UUID.fromString(
                Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .payload
                    .subject
            )
        } catch (e: Exception) {
            logger.debug("JWT validation failed: ${e.message}")
            null
        }
}
