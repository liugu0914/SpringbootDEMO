
package com.jiopeel.core.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class WebUtil implements Serializable {
    private static final long serialVersionUID = 8804935919085171285L;

    public WebUtil() {
    }

    /**
     * 获得request域中的值并转换为Map<String,String>
     *
     *
     * @param request
     *            当前request对象
     * @return Map<String,String>属性名为key,域中值为val
     */
    public static Map<String, String> getParam2Map(HttpServletRequest request) {
        Map<String, String> values = new HashMap<String, String>();
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement();
            String val = request.getParameter(key);
            try {
                val = URLDecoder.decode(val.replaceAll("%", "%25"), "UTF-8");
            } catch (Exception e) {
                log.warn("{}值转码失败!", val);
            }
            values.put(key, val.trim());
        }
        return values;
    }

    public static String getIpAddr(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        log.info("x-forwarded-for : {}",ip);
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            log.info("X-Real-IP : {}",ip);
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info("Proxy-Client-IP : {}",ip);
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info("WL-Proxy-Client-IP : {}",ip);
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info("HTTP_CLIENT_IP : {}",ip);
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info("HTTP_X_FORWARDED_FOR : {}",ip);
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            log.info("request.getRemoteAddr() : {}",ip);
        }
        return ip;
    }

    public static String getMacAddr() {
        Enumeration el = null;
        try {
            el = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        byte[] mac=null;
        do {
            if (!el.hasMoreElements()) {
                return null;
            }

            try {
                mac = ((NetworkInterface) el.nextElement()).getHardwareAddress();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } while (mac == null || mac.length == 0);

        StringBuilder builder = new StringBuilder();
        byte[] var3 = mac;
        int var4 = mac.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            builder.append(hexByte(b));
            builder.append("-");
        }

        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static String getRequestURL(HttpServletRequest request) {
        StringBuilder url = new StringBuilder(request.getRequestURI());
        Map<String, String[]> parameterMap = request.getParameterMap();
        String key = null;
        String[] value = null;
        if (parameterMap != null && parameterMap.size() > 0) {
            url.append("?");
            Iterator it = parameterMap.keySet().iterator();

            while (true) {
                do {
                    do {
                        if (!it.hasNext()) {
                            url.delete(url.length() - 1, url.length());
                            return url.toString();
                        }

                        key = (String) it.next();
                        value = parameterMap.get(key);
                    } while (value == null);
                } while (value.length <= 0);

                String[] var6 = value;
                int var7 = value.length;

                for (int var8 = 0; var8 < var7; ++var8) {
                    String val = var6[var8];
                    url.append(key).append("=").append(val).append("&");
                }
            }
        } else {
            return url.toString();
        }
    }

    public static String getBasePath(HttpServletRequest request) {
        int port = request.getServerPort();
        return request.getScheme() + "://" + request.getServerName() + (port == 80 ? "" : ":" + port) + request.getContextPath();
    }

    public static String getBasePathHttps(HttpServletRequest request) {
        int port = request.getServerPort();
        return request.getScheme() + "://" + request.getServerName() + (port == 80 ? "" : ":" + port) + request.getContextPath();
    }

    public static String requestParametersURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        if (!BaseUtil.empty(queryString)) {
            requestURL.append("?").append(queryString);
        }

        return requestURL.toString();
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return request.getHeader("x-requested-with") != null;
    }

    public static void write(HttpServletResponse response, Object outObj, String outEncoding) throws IOException {
        if (response.getContentType() == null) {
            response.setContentType("text/html");
        }

        response.setCharacterEncoding(outEncoding);
        PrintWriter out = null;
        out = response.getWriter();
        out.print(outObj);
        out.flush();
        out.close();
    }

    private static String hexByte(byte b) {
        String s = "000000" + Integer.toHexString(b);
        return s.substring(s.length() - 2);
    }

    public static void main(String[] args) {
        try {
            System.out.println(getMacAddr());
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}
