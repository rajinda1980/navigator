package org.phinxt.navigator.config;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.phinxt.navigator.utils.TestConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ContentTypeFilterTest {

    private ContentTypeFilter contentTypeFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void init() {
        contentTypeFilter = new ContentTypeFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = Mockito.mock(FilterChain.class);
    }

    @DisplayName("Should pass the filter chain for a valid content type")
    @Test
    void shouldReturnSuccessStatusCode() throws Exception {
        request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        contentTypeFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain).doFilter(request, response);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @DisplayName("Should return a bad response value for invalid content types")
    @Test
    void shouldReturnBadResponseStatusForInvalidContentType() throws Exception {
        request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        contentTypeFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain, Mockito.never()).doFilter(request, response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains(TestConstants.INVALID_CONTENT_TYPE));
    }

    @DisplayName("Should return a bad response for a missing content type")
    @Test
    void shouldReturnBadResponseForMissingContentType() throws Exception {
        contentTypeFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain, Mockito.never()).doFilter(request, response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains(TestConstants.INVALID_CONTENT_TYPE));
    }
}
