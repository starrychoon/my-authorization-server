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

package io.starrychoon.authorizationserver.infrastructure.config

import com.nimbusds.jose.jwk.*
import com.nimbusds.jose.jwk.source.*
import com.nimbusds.jose.proc.*
import io.starrychoon.authorizationserver.infrastructure.jose.*
import org.springframework.context.annotation.*
import org.springframework.core.*
import org.springframework.core.annotation.*
import org.springframework.security.config.annotation.web.builders.*
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.*
import org.springframework.security.config.web.servlet.*
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.core.oidc.*
import org.springframework.security.oauth2.server.authorization.*
import org.springframework.security.oauth2.server.authorization.client.*
import org.springframework.security.oauth2.server.authorization.config.*
import org.springframework.security.web.*
import java.util.*

/**
 * @author starrychoon
 */
@Configuration(proxyBeanMethods = false)
class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        val configurer = OAuth2AuthorizationServerConfigurer<HttpSecurity>()

        http {
            securityMatcher(configurer.endpointsMatcher)
            authorizeRequests {
                authorize(anyRequest, authenticated)
            }
            csrf {
                ignoringRequestMatchers(configurer.endpointsMatcher)
            }
            formLogin { }
        }
        http.apply(configurer)

        return http.build()
    }

    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("messaging-client")
            .clientSecret("{noop}secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
            .redirectUri("http://127.0.0.1:8080/authorized")
            .scope(OidcScopes.OPENID)
            .scope("message.read")
            .scope("message.write")
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build()

        return InMemoryRegisteredClientRepository(registeredClient)
    }

    @Bean
    fun oauth2AuthorizationService(): OAuth2AuthorizationService {
        return InMemoryOAuth2AuthorizationService()
    }

    @Bean
    fun oauth2AuthorizationConsentService(): OAuth2AuthorizationConsentService {
        return InMemoryOAuth2AuthorizationConsentService()
    }

    @Bean
    fun providerSettings(): ProviderSettings {
        return ProviderSettings.builder().issuer("http://auth-server:9000").build()
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey: RSAKey = Jwks.generateRsa()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector, securityContext -> jwkSelector.select(jwkSet) }
    }
}
