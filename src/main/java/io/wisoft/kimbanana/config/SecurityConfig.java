package io.wisoft.kimbanana.config;

import io.wisoft.kimbanana.auth.jwt.JwtAuthenticationFilter;
import io.wisoft.kimbanana.auth.jwt.JwtTokenProvider;
import io.wisoft.kimbanana.auth.oauth.CustomOAuth2UserService;
import io.wisoft.kimbanana.auth.oauth.OAuth2SuccessHandler;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, final OAuth2SuccessHandler oAuth2SuccessHandler, CustomOAuth2UserService customOAuth2UserService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/sign-up",
                                "/api/auth/sign-in",
                                "/api/auth/refresh",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/slide-images/**",
                                "/presentation-thumbnails/**",
                                "/ws-api/**",
                                "/ws/**",
                                "/kimbanana/app/swagger-ui/**",
                                "/kimbanana/app/openapi/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureUrl("/kimbanana/ui?oauth=fail")
                        .failureHandler((req, res, ex) -> {
                            ex.printStackTrace(); // 콘솔에 원인 로그 남김
                            String reason = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
                            res.sendRedirect("https://daisy.wisoft.io/kimbanana/ui?oauth=fail&reason=" + reason);
                        })
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        //허용할 origin
        config.setAllowedOrigins(List.of("https://daisy.wisoft.io", "http://localhost:5173"));

        // 인증 헤더, Content-Type 등 명시적으로 허용
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // 허용 메서드
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 자격증명(Authorization, Cookie 등) 허용 여부
        config.setAllowCredentials(true);

        // preflight 캐시 시간
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }

    @Bean
    org.springframework.web.filter.ForwardedHeaderFilter forwardedHeaderFilter() {
        return new org.springframework.web.filter.ForwardedHeaderFilter();
    }
}
