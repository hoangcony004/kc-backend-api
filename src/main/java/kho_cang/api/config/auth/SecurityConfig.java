package kho_cang.api.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable())
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers(
    //                 "/api/login",
    //                 "/swagger-ui/**",
    //                 "/v3/api-docs/**",
    //                 "/public/**"
    //             ).permitAll()
    //             .anyRequest().authenticated()
    //         )
    //         .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //         .formLogin(form -> form.disable())
    //         .httpBasic(basic -> basic.disable())

    //         // 🔥 Thêm dòng này để kích hoạt filter của bạn
    //         .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

    //     return http.build();
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF vì bạn dùng JWT
            .authorizeRequests(auth -> auth
                .requestMatchers(
                    "/api/login",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/public/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không sử dụng session
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // Thêm các handler cho lỗi xác thực và quyền truy cập
            .exceptionHandling(handling -> handling
                .defaultAuthenticationEntryPointFor(
                    new CustomAuthenticationEntryPoint(), 
                    new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/private/**") // Xử lý lỗi xác thực cho các endpoint
                )
                .accessDeniedHandler(new CustomAccessDeniedHandler()) // Xử lý lỗi quyền truy cập
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}

