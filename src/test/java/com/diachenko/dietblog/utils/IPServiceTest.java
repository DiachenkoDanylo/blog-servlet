package com.diachenko.dietblog.utils;
/*  diet-blog
    28.02.2025
    @author DiachenkoDanylo
*/

import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IPServiceTest {

    @Mock
    private HttpServletRequest request;

    private static final Logger testLogger = LoggerFactory.getLogger(IPService.class);

    @BeforeEach
    void setUp() {
        reset(request);
    }

    @Test
    void testGetClientIp_FromXForwardedFor() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1");

        String ip = IPService.getClientIp(request);

        assertEquals("192.168.1.1", ip);
    }

    @Test
    void testGetClientIp_FromRemoteAddr_WhenXForwardedForIsEmpty() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("");
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");

        String ip = IPService.getClientIp(request);

        assertEquals("10.0.0.1", ip);
    }

    @Test
    void testGetClientIp_FromRemoteAddr_WhenXForwardedForIsUnknown() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("unknown");
        when(request.getRemoteAddr()).thenReturn("172.16.0.1");

        String ip = IPService.getClientIp(request);

        assertEquals("172.16.0.1", ip);
    }

    @Test
    void testLogClientIpWithException() {
        RuntimeException exception = new RuntimeException("Test Exception");
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("203.0.113.5");

        IPService.logClientIpWithException(request, exception);

        assertDoesNotThrow(() -> IPService.logClientIpWithException(request, exception));
    }

    @Test
    void testLogClientIpWithActivity() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("198.51.100.7");

        IPService.logClientIpWithActivity(request, "User login");


        assertDoesNotThrow(() -> IPService.logClientIpWithActivity(request, "User login"));
    }
}
