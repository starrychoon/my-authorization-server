/*
 * Copyright 2020-2021 the original author or authors.
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

package io.starrychoon.authorizationserver.infrastructure.jose

import com.nimbusds.jose.jwk.*
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.RSAKey
import java.security.*
import java.security.interfaces.*
import java.util.*
import javax.crypto.*

/**
 * @author Joe Grandja
 * @since 0.1.0
 */
object Jwks {
    fun generateRsa(): RSAKey {
        val keyPair: KeyPair = KeyGeneratorUtils.generateRsaKey()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey

        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }

    fun generateEc(): ECKey {
        val keyPair: KeyPair = KeyGeneratorUtils.generateEcKey()
        val publicKey = keyPair.public as ECPublicKey
        val privateKey = keyPair.private as ECPrivateKey
        val curve = Curve.forECParameterSpec(publicKey.params)

        return ECKey.Builder(curve, publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }

    fun generateSecret(): OctetSequenceKey {
        val secretKey: SecretKey = KeyGeneratorUtils.generateSecretKey()

        return OctetSequenceKey.Builder(secretKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }
}
