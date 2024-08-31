package com.dynamics.api_gateway.filter;

import io.jsonwebtoken.*;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;
import java.util.Set;

@Component
public class JwtAuthenticationFilter implements GatewayFilter, Ordered {

    //tbd entfernen von hardgecodetem und auch oben in api gateway
    private final String jwtSecret = Base64.getEncoder().encodeToString("aslkdfjehghöfdgoiegferjgrelgaj".getBytes());;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> headers = exchange.getRequest().getHeaders().get("Authorization");
        if (headers == null || headers.isEmpty() || !headers.get(0).startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = headers.get(0).substring(7);
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            String userId = claims.get("userId", String.class);
            List<String> roles = claims.get("roles", List.class);
            exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header("X-User-ID", userId)
                            .header("X-User-ROLES", String.join(",", roles))
                            .build()).build();

        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException e) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

