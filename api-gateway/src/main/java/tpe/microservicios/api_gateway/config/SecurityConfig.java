package tpe.microservicios.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tpe.microservicios.api_gateway.security.AuthotityConstant;
import tpe.microservicios.api_gateway.security.jwt.TokenProvider;
import tpe.microservicios.api_gateway.security.jwt.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    public SecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Endpoints de autenticación
                        .requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                        // Endpoints de microservicios según tu lista

                        .requestMatchers("/api/accounts/**").hasAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers("/api/monopatines/**").hasAuthority(AuthotityConstant._USUARIO)
                        .requestMatchers("/api/paradas/**").hasAuthority(AuthotityConstant._USUARIO)
                        .requestMatchers("/api/admin/**").hasAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers("/api/viajes/**").hasAuthority(AuthotityConstant._USUARIO)
                        .requestMatchers("/api/reportes/**").hasAuthority(AuthotityConstant._ADMIN)

                        // Cualquier otro endpoint requiere autenticación
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(new JwtFilter(this.tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
