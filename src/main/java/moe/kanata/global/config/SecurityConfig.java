package moe.kanata.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.kanata.auth.api.AccessTokenFilter;
import moe.kanata.auth.api.AuthorizationRequestRedirectResolver;
import moe.kanata.auth.api.OAuth2LoginSuccessHandler;
import moe.kanata.auth.api.TokenExceptionFilter;
import moe.kanata.auth.service.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService userService;
    private final OAuth2LoginSuccessHandler loginSuccessHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AuthorizationRequestRedirectResolver authorizationRequestRedirectResolver;
    private final AccessTokenFilter accessTokenFilter;

    public static final List<String> clients = List.of(
        "http://localhost:3000",
        "http://localhost:8080"
    );

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/oauth2/**",
                    "/auth/**",
                    "/login/**"
                )
                .permitAll()
                .anyRequest()
                .hasAnyRole("USER")
            )
            .oauth2Login(oauth2 -> oauth2.redirectionEndpoint(redirection ->
                    redirection.baseUri("/login/oauth2/code/{registrationId}"))
                .userInfoEndpoint(userInfoEndpoint ->
                    userInfoEndpoint.userService(userService)
                )
                .loginProcessingUrl("/auth/login")
                .authorizationEndpoint(authorization ->
                    authorization.authorizationRequestResolver(authorizationRequestRedirectResolver)
                )
                .successHandler(loginSuccessHandler))
            .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new TokenExceptionFilter(), accessTokenFilter.getClass())
            .exceptionHandling(exceptions ->
                exceptions.authenticationEntryPoint(authenticationEntryPoint));
      return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(clients);
        configuration.setAllowedMethods(Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.PUT.name(),
            HttpMethod.DELETE.name()
        ));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
