package com.gscotrim.mate.shared.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

@Service
class JwtService(
    @param:Value("\${jwt.secret}")
    private val secret: String,
    @param:Value("\${jwt.expiration}")
    private val expirationSeconds: Long
) {
    private val logger = LoggerFactory.getLogger(JwtService::class.java)
    private val key = Keys.hmacShaKeyFor(secret.trim().toByteArray())

    fun generateToken(userId: UUID): JwtToken {
        val now = Date()
        return JwtToken(
            Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(Date(now.time + expirationSeconds.seconds.inWholeMilliseconds))
                .signWith(key)
                .compact()
        )
    }

    fun extractUserId(token: JwtToken): UUID? =
        try {
            UUID.fromString(
                Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token.toString())
                    .payload
                    .subject
            )
        } catch (e: JwtException) {
            when (e) {
                is ExpiredJwtException -> logger.debug("JWT expired: ${e.message}")
                is MalformedJwtException -> logger.debug("JWT malformed: ${e.message}")
                is SignatureException -> logger.debug("JWT signature invalid: ${e.message}")
                else -> logger.debug("JWT validation failed: ${e.message}")
            }
            null
        } catch (e: Exception) {
            logger.warn("Unexpected error during JWT validation: ${e.message}")
            null
        }
}
