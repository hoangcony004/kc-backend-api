package kho_cang.api.config.auth;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        System.out.println("🔥 JWT Filter đang xử lý: " + req.getRequestURI());
        System.out.println("Request Path: " + req.getRequestURI());

        String path = req.getRequestURI();
        if (path.startsWith("/api/private/")) {
            String authHeader = req.getHeader("Authorization");
            System.out.println("Authorization header: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    if (jwtService.isValidToken(token)) {
                        System.out.println("✅ Token hợp lệ");
                        String username = jwtService.extractUsername(token);
                        List<String> roles = jwtService.extractRoles(token);

                        // Chuyển đổi roles thành GrantedAuthority (thêm tiền tố ROLE_ nếu cần)
                        List<GrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Thêm ROLE_ nếu cần
                            .collect(Collectors.toList());

                        // Tạo và gán Authentication vào SecurityContext
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, authorities
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        chain.doFilter(request, response);
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("❌ Lỗi xử lý token: " + e.getMessage());
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().write("Token không hợp lệ: " + e.getMessage());
                    return;
                }
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Token không hợp lệ.");
            } else {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().write("Thiếu token.");
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}