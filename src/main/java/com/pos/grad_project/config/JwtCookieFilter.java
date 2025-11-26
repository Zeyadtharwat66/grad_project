package com.pos.grad_project.config;
import com.pos.grad_project.service.JWTTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
public class JwtCookieFilter extends OncePerRequestFilter {
    private final JWTTokenService jwtTokenService;
    public JwtCookieFilter(JWTTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    try {
                        Authentication auth = jwtTokenService.parseJWT(token);
                        if (auth != null) {
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid JWT Cookie");
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}