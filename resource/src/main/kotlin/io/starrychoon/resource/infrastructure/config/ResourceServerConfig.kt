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

package io.starrychoon.resource.infrastructure.config

import org.springframework.context.annotation.*
import org.springframework.security.config.annotation.web.reactive.*
import org.springframework.security.config.web.server.*
import org.springframework.security.web.server.*
import org.springframework.security.web.server.util.matcher.*

/**
 * @author starrychoon
 */
@EnableWebFluxSecurity
class ResourceServerConfig {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            securityMatcher(PathPatternParserServerWebExchangeMatcher("/messages/**"))
            authorizeExchange {
                authorize("/messages/**", hasAuthority("SCOPE_message.read"))
            }
            oauth2ResourceServer {
                jwt { }
            }
        }
    }
}
