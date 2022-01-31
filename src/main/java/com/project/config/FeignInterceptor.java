package com.project.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Context;

@Component
public class FeignInterceptor implements RequestInterceptor {

    @Context
    javax.ws.rs.core.SecurityContext securityContext;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (securityContext != null && securityContext.getUserPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal principal = ((KeycloakPrincipal) securityContext.getUserPrincipal());
            AccessToken token = principal.getKeycloakSecurityContext().getToken();
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}

