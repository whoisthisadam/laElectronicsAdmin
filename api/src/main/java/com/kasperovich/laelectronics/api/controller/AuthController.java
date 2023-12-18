package com.kasperovich.laelectronics.api.controller;

import com.kasperovich.laelectronics.api.dto.auth.AuthRequest;
import com.kasperovich.laelectronics.api.dto.auth.AuthResponse;
import com.kasperovich.laelectronics.api.security.jwt.TokenUtils;
import com.kasperovich.laelectronics.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final TokenUtils tokenUtils;

    private final UserDetailsService userProvider;

    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request) {

        /*Check login and password*/
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailOrLogin(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        /*Generate token with answer to user*/
        return ResponseEntity.ok(
                AuthResponse
                        .builder()
                        .userNameOrEmail(request.getEmailOrLogin())
                        .token(tokenUtils.generateToken(userProvider.loadUserByUsername(request.getEmailOrLogin())))
                        .userId(
                                userRepository.findUserByEmailAndIsDeleted(request.getEmailOrLogin(), false)
                                .orElseThrow(EntityNotFoundException::new).getId()
                        )
                        .build()
        );
    }

}
