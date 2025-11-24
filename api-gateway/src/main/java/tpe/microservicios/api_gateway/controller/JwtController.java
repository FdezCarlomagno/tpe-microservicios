package tpe.microservicios.api_gateway.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tpe.microservicios.api_gateway.clients.AdminClient;
import tpe.microservicios.api_gateway.security.AuthorityConstant;
import tpe.microservicios.api_gateway.security.jwt.JwtFilter;
import tpe.microservicios.api_gateway.security.jwt.TokenProvider;
import tpe.microservicios.api_gateway.service.dto.login.LoginDTO;

import java.util.List;

@RestController
@RequestMapping("/api/authenticate" )
@RequiredArgsConstructor
public class JwtController {
    private final TokenProvider tokenProvider;
    private final AdminClient adminClient;

    @PostMapping
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginDTO request) {

        // 1. Validar admin contra microservicio
        boolean isValid = adminClient.login(request);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Crear Authentication manual con rol ADMIN
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_"+ AuthorityConstant._ADMIN));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                null,
                authorities
        );

        // 3. Generar JWT
        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new JWTToken(jwt), headers, HttpStatus.OK);
    }

    static class JWTToken {
        @JsonProperty("id_token")
            private String idToken;

            public JWTToken(String idToken) {
                this.idToken = idToken;
            }

    }
}
