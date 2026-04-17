package com.gscotrim.mate.shared.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class JwtAuthFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
        private const val ROLE_USER = "ROLE_USER"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = request.getHeader(AUTHORIZATION_HEADER)

        if (headerIsValid(header)) {
            val token = JwtToken(header.removePrefix(BEARER_PREFIX))
            val userId = jwtService.extractUserId(token)

            if (shouldAuthenticate(userId)) {
                val context = SecurityContextHolder.createEmptyContext()
                context.authentication = UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    listOf(SimpleGrantedAuthority(ROLE_USER))
                )
                SecurityContextHolder.setContext(context)
            }
        }

        try {
            chain.doFilter(request, response)
        } finally {
            SecurityContextHolder.clearContext()
        }
    }

    private fun headerIsValid(header: String?): Boolean {
        return header != null && header.startsWith(BEARER_PREFIX)
    }

    private fun shouldAuthenticate(userId: UUID?): Boolean {
        return userId != null && SecurityContextHolder.getContext().authentication == null
    }
}
