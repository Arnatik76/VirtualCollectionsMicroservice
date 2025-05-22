package kz.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                                .pathMatchers("/vc-users/auth/**").permitAll()
                                .pathMatchers("/actuator/**").permitAll()
                                .pathMatchers(HttpMethod.GET, "/vc-collections/collections/*/items").permitAll()
                                .anyExchange().permitAll()
                );
        return http.build();
    }
}