package com.dynamics.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RoleAuthorizationFilter implements GatewayFilter {

    private Set<String> requiredRoles;

    public RoleAuthorizationFilter(Set<String> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> roleIds = exchange.getRequest().getHeaders().get("X-User-ROLES");

        if (roleIds == null || !containsRequiredRole(roleIds)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private boolean containsRequiredRole(List<String> roleIds) {
        Map<String, String> roleMap = new HashMap<>();
        roleMap.put("123456", "ADMIN");
        roleMap.put("234567", "USER");

        for (String roleId : roleIds) {
            String roleName = roleMap.get(roleId);
            if (roleName != null && requiredRoles.contains(roleName)) {
                return true;
            }
        }
        return false;
    }
}
