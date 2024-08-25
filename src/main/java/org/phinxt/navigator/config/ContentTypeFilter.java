package org.phinxt.navigator.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.phinxt.navigator.utils.AppConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class ContentTypeFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (!getSkipUrlList().contains(request.getRequestURI()) &&
                (null == contentType || !contentType.contains(MediaType.APPLICATION_JSON_VALUE))) {
            log.info("Invalid request is received. Content-Type : {}", contentType);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(AppConstants.INVALID_CONTENT_TYPE);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private List<String> getSkipUrlList() {
        return List.of(
                "/swagger-ui/index.html",
                "/swagger-ui/index.css",
                "/swagger-ui/swagger-initializer.js",
                "/swagger-ui/swagger-ui.css",
                "/swagger-ui/swagger-ui-standalone-preset.js",
                "/swagger-ui/swagger-ui-bundle.js",
                "/swagger-ui/favicon-32x32.png",
                "/swagger-ui/favicon-16x16.png",
                "/v3/api-docs/swagger-config",
                "/v3/api-docs"
        );
    }
}
