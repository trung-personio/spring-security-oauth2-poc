package com.home.intp.oauth

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/oauth2/{registrationId}/callback")
class OAuth2CallbackController {
    // Authorized client is saved in session by default, it can be saved in DB with following schema:
    // https://github.com/spring-projects/spring-security/blob/de8b558561863cec93ec20e07f913d7e439fa090/oauth2/oauth2-client/src/main/resources/org/springframework/security/oauth2/client/oauth2-client-schema.sql
    @GetMapping
    fun get(
        @RegisteredOAuth2AuthorizedClient("slack") oAuth2AuthorizedClient: OAuth2AuthorizedClient
    ): AccessToken {
        return AccessToken(
            oAuth2AuthorizedClient.accessToken.tokenValue,
            oAuth2AuthorizedClient.refreshToken?.tokenValue ?: ""
        )
    }
}
