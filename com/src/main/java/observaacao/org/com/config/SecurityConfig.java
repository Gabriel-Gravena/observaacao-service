package observaacao.org.com.config;

import observaacao.org.com.auth.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/auth/signUp").permitAll()
                        .requestMatchers("/auth/signIn").permitAll()
                        .requestMatchers("/common/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/categorias/**").hasRole("SERVIDOR")
                        .requestMatchers(HttpMethod.PUT, "/categorias/**").hasRole("SERVIDOR")
                        .requestMatchers(HttpMethod.DELETE, "/categorias/**").hasRole("SERVIDOR")
                        .requestMatchers(HttpMethod.GET, "/solicitacoes").hasRole("SERVIDOR")
                        .requestMatchers(HttpMethod.GET, "/solicitacoes/atrasadas").hasRole("SERVIDOR")
                        .requestMatchers(HttpMethod.PATCH, "/solicitacoes/*/status").hasRole("SERVIDOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
