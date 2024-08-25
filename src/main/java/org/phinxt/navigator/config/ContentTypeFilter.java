package org.phinxt.navigator.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.phinxt.navigator.utils.AppConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ContentTypeFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (null == contentType || !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(AppConstants.INVALID_CONTENT_TYPE);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
