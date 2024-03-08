package org.example.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {
    public static String getIP(HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip.trim())) {
            int index = ip.indexOf(",");
            if (index != -1) {
                ip = ip.substring(0, index);
            }
            return ip;
        } else {
            ip = httpServletRequest.getHeader("X-Real-IP");
            if (ip == null || "".equals(ip.trim())) {
                ip = httpServletRequest.getRemoteAddr();
            }
            return ip;
        }
    }
}
