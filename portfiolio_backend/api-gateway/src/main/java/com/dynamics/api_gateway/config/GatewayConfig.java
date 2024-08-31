package com.dynamics.api_gateway.config;

import com.dynamics.api_gateway.filter.JwtAuthenticationFilter;
import com.dynamics.api_gateway.filter.RoleAuthorizationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtAuthenticationFilter) {
        return builder.routes()
                // Route for portfolio-asset/values without JWT authentication
                .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.rewritePath("/api/auth/(?<segment>.*)", "/auth/${segment}"))
                        .uri("lb://user-service"))
                .route("portfolio-get-details-with-access-code-service", r -> r.path("/api/portfolio/{id}/verify-access-code")
                        .and().method("GET")
                        .filters(f -> f.rewritePath("/api/portfolio/(?<id>.*)/verify-access-code", "/portfolio/${id}/verify-access-code"))
                        .uri("lb://portfolio-service"))
                .route("portfolio-get-all-portfolios-meta-data", r -> r.path("/api/portfolio/")
                        .and().method("GET")
                        .filters(f -> f.rewritePath("/api/portfolio/", "/portfolio/"))
                        .uri("lb://portfolio-service"))



                // Route with JWT authentication
                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter)
                                .rewritePath("/api/users/(?<segment>.*)", "/users/${segment}"))
                        .uri("lb://user-service"))
                .route("portfolio-service", r -> r.path("/api/portfolio/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter)
                                .rewritePath("/api/portfolio/(?<segment>.*)", "/portfolio/${segment}"))
                        .uri("lb://portfolio-service"))
                .route("asset-service-create-only-admin", r -> r.path("/api/asset/create")
                        .filters(f -> f.filter(jwtAuthenticationFilter)
                                .filter(new RoleAuthorizationFilter(Set.of("ADMIN" )))
                                .rewritePath("/api/asset/create","/asset/create"))
                        .uri("lb://portfolio-service"))
                .route("asset-service", r -> r.path("/api/asset/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter)
                                .rewritePath("/api/asset/(?<segment>.*)", "/asset/${segment}"))
                        .uri("lb://portfolio-service"))
                .route("portfolio-asset-service", r -> r.path("/api/portfolio-asset/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter)
                                .rewritePath("/api/portfolio-asset/(?<segment>.*)", "/portfolio-asset/${segment}"))
                        .uri("lb://portfolio-service"))

                //default
                .route("default-service", r -> r.path("/api/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter)
                                .rewritePath("/api/(?<service>.+)/(?<segment>.*)", "/${service}/${segment}"))
                        .uri("lb://portfolio-service"))
                .build();


    }
}

