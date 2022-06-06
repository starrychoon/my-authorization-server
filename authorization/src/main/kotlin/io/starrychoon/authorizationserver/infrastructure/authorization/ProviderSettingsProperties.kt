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

package io.starrychoon.authorizationserver.infrastructure.authorization

import org.springframework.boot.context.properties.*

/**
 * @author starrychoon
 *
 * @property issuer The URL the Provider uses as its Issuer Identifier.
 * @property jwkSet The JWK set
 */
@ConfigurationProperties(prefix = "provider.settings")
@ConstructorBinding
class ProviderSettingsProperties(
    val issuer: String?,
    val jwkSet: List<JWKInfo>,
) {

    /**
     * @property publicKeyPath The resource location of PEM-encoded public key. It can be `null` if [privateKeyPath] is RSA private key.
     * @property privateKeyPath The resource location of PEM-encoded private key.
     * @property keyId key ID of JWK
     */
    class JWKInfo(
        val publicKeyPath: String?,
        val privateKeyPath: String,
        val keyId: String?,
    )
}
