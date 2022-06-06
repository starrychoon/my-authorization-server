/*
 * Copyright 2022-2022 starrychoon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.starrychoon.authorizationserver.infrastructure.nimbusds

import com.nimbusds.jose.jwk.*

/**
 * @author starrychoon
 */
class JWKBuilder(private val jwk: JWK) {
    private var use: KeyUse? = null
    private var kid: String? = null

    fun keyUse(use: KeyUse?) = apply { this.use = use }

    fun keyID(kid: String?) = apply { this.kid = kid }

    fun build(): JWK {
        return when (jwk) {
            is RSAKey -> buildRSAKey()
            is ECKey -> buildECKey()
            is OctetSequenceKey -> buildOctetSequenceKey()
            is OctetKeyPair -> buildOctetKeyPair()
            else -> error("Unknown JWK type! key type is ${jwk.keyType}.")
        }
    }

    private fun buildRSAKey(): RSAKey {
        return RSAKey.Builder(jwk.toRSAKey())
            .keyUse(use)
            .keyID(kid)
            .build()
    }

    private fun buildECKey(): ECKey {
        return ECKey.Builder(jwk.toECKey())
            .keyUse(use)
            .keyID(kid)
            .build()
    }

    private fun buildOctetSequenceKey(): OctetSequenceKey {
        val octetSequenceKey = jwk.toOctetSequenceKey()
        return OctetSequenceKey.Builder(octetSequenceKey.toSecretKey())
            .keyUse(use)
            .keyID(kid)
            .build()
    }

    private fun buildOctetKeyPair(): OctetKeyPair {
        return OctetKeyPair.Builder(jwk.toOctetKeyPair())
            .keyUse(use)
            .keyID(kid)
            .build()
    }
}

fun JWK.builder(): JWKBuilder {
    return JWKBuilder(this)
}
