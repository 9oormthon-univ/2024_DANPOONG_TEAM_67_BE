package goormton.backend.somgil.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import goormton.backend.somgil.global.config.security.jwt.domain.TokenProvider;
import goormton.backend.somgil.global.config.security.jwt.errorHandler.CustomAccessDeniedHandler;
import goormton.backend.somgil.global.config.security.jwt.errorHandler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        cors 설정
        http
                .cors(withDefaults());
//        csrf 설정
        http
                .csrf((auth) -> auth.disable());
//        Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());
//        Http Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
//        세션 설정: STATELESS
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                );
//        JWT Filter 추가
        http
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider,objectMapper), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception->exception.accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper)));
        return http.build();
    }

//    비밀번호 인코더 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    인증 관리자 설정
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
