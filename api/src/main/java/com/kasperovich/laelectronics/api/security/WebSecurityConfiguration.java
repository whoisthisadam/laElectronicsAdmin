package com.kasperovich.laelectronics.api.security;


import com.kasperovich.laelectronics.api.security.filter.AuthenticationTokenFilter;
import com.kasperovich.laelectronics.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final AuthenticationTokenFilter authFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // http basic authentication
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll() // permit all get requests
                                .requestMatchers("/api/v1/auth/**").permitAll() // permit all auth requests
                                .requestMatchers("/swagger", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                                .requestMatchers("/data/**").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**", "/configuration/ui/**", "/swagger-resources/**", "/configuration/security/**", "/swagger-ui/**", "/webjars/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/guest/**").permitAll()
                                .requestMatchers("/registration/**").permitAll()
                                .requestMatchers("/authentication/**").permitAll()
                                .requestMatchers("/test/**").permitAll()
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "MODERATOR")
                                .anyRequest().authenticated() // all other requests must be authenticated
                )
                .exceptionHandling(exc -> exc.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //disable session creation

        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Component("userSecurity")
    public class UserSecurity {
        public boolean hasUserId(Authentication authentication, String userId) {
            Long id = Long.parseLong(userId);
            String email=userRepository.findUserById(id).orElseThrow(
                    ()->new BadCredentialsException("This user does not exist"))
                    .getEmail();
            UserDetails userDetails=(UserDetails) authentication.getPrincipal();
            return userDetails.getUsername().equals(email);
        }
    }
}
