package com.example.gatewayservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final List<String> publicPaths = List.of(
            "/api/users/login",
            "/api/users/register",
            "/api/users/verify-code",
            "/api/users/reset-password"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        System.out.println("🔍 Gateway request: " + method + " " + path);

        // ✅ Allow all OPTIONS preflight requests (CORS)
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            System.out.println("🟡 OPTIONS preflight allowed");
            return chain.filter(exchange);
        }

        // ✅ Allow public endpoints without authentication
        if (publicPaths.stream().anyMatch(path::startsWith)) {
            System.out.println("🔓 Public path, no token required");
            return chain.filter(exchange);
        }

        // ❌ Reject if no valid token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Missing or invalid Authorization header");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            System.out.println("✅ JWT OK — userId: " + userId + ", role: " + role);

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("Authorization", authHeader)
                    .header("X-User-Id", userId)
                    .header("X-User-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            System.out.println("❌ JWT failed: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT validation failed: " + e.getMessage());
        }
    }

    @Override
    public int getOrder() {
        return -1; // Run early
    }
}
