package com.diachenko.dietblog.utils;
/*  diet-blog
    21.02.2025
    @author DiachenkoDanylo
*/

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class IPService {

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static void logClientIpWithException(HttpServletRequest request, RuntimeException e) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        log.error("{} ClientIp={}", e.getMessage(), ip);
    }

    public static void logClientIpWithActivity(HttpServletRequest request, String activity) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        log.warn("{} ClientIp={}", activity, ip);
    }
}
