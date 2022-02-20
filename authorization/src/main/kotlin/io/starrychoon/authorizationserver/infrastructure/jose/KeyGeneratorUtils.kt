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

import java.math.*
import java.security.*
import java.security.spec.*
import javax.crypto.*

/**
 * @author Joe Grandja
 * @since 0.1.0
 */
internal object KeyGeneratorUtils {
    fun generateSecretKey(): SecretKey {
        return KeyGenerator.getInstance("HmacSha256").generateKey()
    }

    fun generateRsaKey(): KeyPair {
        return KeyPairGenerator.getInstance("RSA").run {
            initialize(2048)
            generateKeyPair()
        }
    }

    fun generateEcKey(): KeyPair {
        val ellipticCurve = EllipticCurve(
            ECFieldFp(BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951")),
            BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948"),
            BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291"),
        )
        val ecPoint = ECPoint(
            BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"),
            BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109"),
        )
        val ecParameterSpec = ECParameterSpec(
            ellipticCurve,
            ecPoint,
            BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"),
            1,
        )

        return KeyPairGenerator.getInstance("EC").run {
            initialize(ecParameterSpec)
            generateKeyPair()
        }
    }
}
