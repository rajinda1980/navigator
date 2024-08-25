#### Assumptions

1. Patches can be null or empty. This means coordinates for dirty patches are not provided with the request, so the application assumes there are no dirty patches to be cleaned by the hoover.
2. Instructions can be null or empty. This means the hoover will not change its location.
3. The application accepts only application/json requests.
4. However, the application allows any website to interact with the API since the requirement does not specify which origin should be allowed to access the API. Once the requirement is clarified, CorsConfig can be explicitly updated with allowed origins based on trusted domains.
   ```
   corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
   ```
5. The Authorization header (e.g., for authorization tokens) has not been used since the application does not use any token-based communication. If the API is updated to use tokens, such as JWT tokens generated from an OAuth server, then the Authorization header can be added to the setAllowedHeaders attribute.
   ```
   corsConfiguration.setAllowedHeaders(Arrays.asList(HttpHeaders.ORIGIN, HttpHeaders.CONTENT_TYPE));
   ```
6. Currently, the API supports only POST requests. Therefore, the CORS configuration allows only POST calls. This can be enhanced with other methods if the API is updated to support those methods in the future.
   ```
        corsConfiguration.setAllowedMethods(
                Arrays.asList(
                        HttpMethod.POST.name()
                )
        )
   ```