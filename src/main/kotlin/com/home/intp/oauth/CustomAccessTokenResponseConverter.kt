package com.home.intp.oauth

import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames

/**
 * The custom converter to transform access token exchange from code response body to data model.
 *
 * The reason of overriding because the default converter requires tokenType under response body
 * as a mandatory field from requirement. But Slack does not return it.
 */
class CustomAccessTokenResponseConverter : Converter<Map<String, Any>, OAuth2AccessTokenResponse> {
    override fun convert(tokenResponseParameters: Map<String, Any>): OAuth2AccessTokenResponse {
        val accessToken: String = tokenResponseParameters[OAuth2ParameterNames.ACCESS_TOKEN].toString()
        val refreshToken: String = tokenResponseParameters[OAuth2ParameterNames.REFRESH_TOKEN].toString()
        val accessTokenType: TokenType = TokenType.BEARER
        var expiresIn: Long = 0
        if (tokenResponseParameters.containsKey(OAuth2ParameterNames.EXPIRES_IN)) {
            expiresIn = tokenResponseParameters[OAuth2ParameterNames.EXPIRES_IN].toString().toLong()
        }
        var scopes: Set<String?> = emptySet<String>()
        if (tokenResponseParameters.containsKey(OAuth2ParameterNames.SCOPE)) {
            val scope: String = tokenResponseParameters[OAuth2ParameterNames.SCOPE]!!.toString()
            scopes = scope.split(" ").toSet()
        }
        val additionalParameters: MutableMap<String?, Any?> = LinkedHashMap()
        tokenResponseParameters.entries.stream()
            .filter { (key): Map.Entry<String, Any> ->
                !TOKEN_RESPONSE_PARAMETER_NAMES.contains(
                    key
                )
            }
            .forEach { (key, value): Map.Entry<String, Any> ->
                additionalParameters[key] = value
            }
        return OAuth2AccessTokenResponse.withToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType(accessTokenType)
            .expiresIn(expiresIn)
            .scopes(scopes)
            .additionalParameters(additionalParameters)
            .build()
    }

    companion object {
        private val TOKEN_RESPONSE_PARAMETER_NAMES: Set<String> = setOf(
            OAuth2ParameterNames.ACCESS_TOKEN,
            OAuth2ParameterNames.REFRESH_TOKEN,
            OAuth2ParameterNames.TOKEN_TYPE,
            OAuth2ParameterNames.EXPIRES_IN,
            OAuth2ParameterNames.REFRESH_TOKEN,
            OAuth2ParameterNames.SCOPE
        )
    }
}
