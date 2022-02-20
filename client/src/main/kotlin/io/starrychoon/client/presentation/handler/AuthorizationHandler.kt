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

package io.starrychoon.client.presentation.handler

import kotlinx.coroutines.reactor.*
import org.springframework.beans.factory.annotation.*
import org.springframework.security.authentication.*
import org.springframework.security.core.*
import org.springframework.security.core.authority.*
import org.springframework.security.oauth2.client.*
import org.springframework.security.oauth2.client.authentication.*
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.*
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.core.endpoint.*
import org.springframework.stereotype.*
import org.springframework.web.reactive.function.client.*
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.server.*
import reactor.core.publisher.*
import reactor.kotlin.core.publisher.cast
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.util.function.*

/**
 * @author starrychoon
 */
@Component
class AuthorizationHandler(
    @Value("\${messages.base-uri}")
    private val messagesBaseUri: String,
    private val webClient: WebClient,
    private val authorizedClientManager: ReactiveOAuth2AuthorizedClientManager,
) {

    suspend fun authorizationFailed(request: ServerRequest): ServerResponse {
        val errorCode = request.queryParamOrNull(OAuth2ParameterNames.ERROR)
        val model = mutableMapOf<String, Any>()
        if (!errorCode.isNullOrBlank()) {
            model["error"] = OAuth2Error(errorCode,
                request.queryParamOrNull(OAuth2ParameterNames.ERROR_DESCRIPTION),
                request.queryParamOrNull(OAuth2ParameterNames.ERROR_URI))
        }

        return ok().renderAndAwait("index", model)
    }

    suspend fun authorizationCodeGrant(request: ServerRequest): ServerResponse {
        val authorizedClient = authorizedClient(request, "messaging-client-authorization-code").awaitSingle()
        val messages = webClient
            .get()
            .uri(messagesBaseUri)
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono<List<String>>()
            .awaitSingle()
        val model = mapOf("messages" to messages)

        return ok().renderAndAwait("index", model)
    }

    suspend fun clientCredentialsGrant(request: ServerRequest): ServerResponse {
        val messages = webClient
            .get()
            .uri(messagesBaseUri)
            .attributes(clientRegistrationId("messaging-client-client-credentials"))
            .retrieve()
            .bodyToMono<List<String>>()
            .awaitSingle()
        val model = mapOf("messages" to messages)

        return ok().renderAndAwait("index", model)
    }

    private fun authorizedClient(request: ServerRequest, registrationId: String? = null): Mono<OAuth2AuthorizedClient> {
        return authorizeRequest(request, registrationId).flatMap(authorizedClientManager::authorize)
    }

    private fun authorizeRequest(request: ServerRequest, registrationId: String?): Mono<OAuth2AuthorizeRequest> {
        val defaultAuthentication = request.exchange()
            .getPrincipal<Authentication>()
            .defaultIfEmpty(ANONYMOUS_USER_TOKEN)
        val defaultRegistrationId = Mono.justOrEmpty(registrationId)
            .switchIfEmpty { clientRegistrationId(defaultAuthentication) }
            .switchIfEmpty { Mono.error { IllegalArgumentException("The clientRegistrationId could not be resolved. Please provide one") } }

        return Mono.zip(defaultRegistrationId, defaultAuthentication)
            .map { (defaultRegistrationId, defaultAuthentication) ->
                OAuth2AuthorizeRequest.withClientRegistrationId(defaultRegistrationId)
                    .principal(defaultAuthentication)
                    .attribute(ServerWebExchange::javaClass.name, request.exchange())
                    .build()
            }
    }

    private fun clientRegistrationId(authentication: Mono<Authentication>): Mono<String> {
        return authentication.filter { it is OAuth2AuthenticationToken }
            .cast<OAuth2AuthenticationToken>()
            .map { it.authorizedClientRegistrationId }
    }

    companion object {
        private val ANONYMOUS_USER_TOKEN: AnonymousAuthenticationToken =
            AnonymousAuthenticationToken("anonymous", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_USER"))
    }
}
