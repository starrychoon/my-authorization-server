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

package io.starrychoon.client.infrastructure.config

import org.springframework.context.annotation.*
import org.springframework.security.oauth2.client.*
import org.springframework.security.oauth2.client.registration.*
import org.springframework.security.oauth2.client.web.*
import org.springframework.security.oauth2.client.web.reactive.function.client.*
import org.springframework.security.oauth2.client.web.server.*
import org.springframework.web.reactive.function.client.*

/**
 * @author starrychoon
 */
@Configuration(proxyBeanMethods = false)
class WebClientConfig {

    @Bean
    fun webClient(
        builder: WebClient.Builder,
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager,
    ): WebClient {
        val oauth2Client = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        return builder.filter(oauth2Client).build()
    }

    @Bean
    fun authorizedClientManager(
        clientRegistrationRepository: ReactiveClientRegistrationRepository,
        authorizedClientRepository: ServerOAuth2AuthorizedClientRepository,
    ): ReactiveOAuth2AuthorizedClientManager {
        val authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .refreshToken()
            .clientCredentials()
            .build()

        return DefaultReactiveOAuth2AuthorizedClientManager(
            clientRegistrationRepository,
            authorizedClientRepository,
        ).apply {
            setAuthorizedClientProvider(authorizedClientProvider)
        }
    }
}
