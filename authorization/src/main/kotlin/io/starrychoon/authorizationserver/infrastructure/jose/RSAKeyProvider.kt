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

package io.starrychoon.authorizationserver.infrastructure.jose

import com.nimbusds.jose.jwk.*
import io.starrychoon.authorizationserver.infrastructure.authorization.*
import mu.*
import org.springframework.core.io.*
import org.springframework.stereotype.*

/**
 * @author starrychoon
 */
@Component
class RSAKeyProvider(
    private val providerSettingsProperties: ProviderSettingsProperties,
    private val resourceLoader: ResourceLoader,
) {
    private val logger = KotlinLogging.logger { }

    fun loadRsaKeySet(): List<RSAKey> {
        return providerSettingsProperties.rsa.mapNotNull(::loadRsaKey)
    }

    private fun loadRsaKey(rsa: ProviderSettingsProperties.RSAKey): RSAKey? {
        try {
            val publicKey = resourceLoader.getResource(rsa.publicKey)
            val privateKey = resourceLoader.getResource(rsa.privateKey)

            if (!publicKey.exists() || !privateKey.exists()) {
                logger.debug { "RSA public key / private key does not exist." }
                return null
            }

            val publicKeyText = publicKey.inputStream.bufferedReader().use { it.readText() }
            val privateKeyText = privateKey.inputStream.bufferedReader().use { it.readText() }
            val rsaKey = JWK.parseFromPEMEncodedObjects(publicKeyText + privateKeyText) as RSAKey

            return RSAKey.Builder(rsaKey)
                .keyID(rsa.keyId)
                .build()
        } catch (e: Exception) {
            logger.warn(e) { "cannot load RSA public key / private key." }
            return null
        }
    }
}
