package com.xhs.config;

import com.xhs.common.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS requests are allowed
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtils.parseToken(token);
            if (claims != null) {
                // Set userId to request attribute for controllers to use if needed
                request.setAttribute("userId", claims.get("userId", Long.class));
                return true;
            }
        }
        
        response.setStatus(401);
        response.getWriter().write("{\"code\": 401, \"message\": \"Unauthorized\", \"data\": null}");
        response.setContentType("application/json");
        return false;
    }
}
