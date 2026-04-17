package com.gscotrim.mate.shared.security

import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class JwtToken(@get:JsonValue val value: String) {
    override fun toString() = value
}