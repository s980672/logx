package com.sktechx.palab.logx;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsHeaderFilter extends OncePerRequestFilter {
    private static final String  ACCESS_CONTROL_ALLOW_ORIGIN      = "Access-Control-Allow-Origin";
    private static final String  ACCESS_CONTROL_ALLOW_METHODS     = "Access-Control-Allow-Methods";
    private static final String  ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String  ACCESS_CONTROL_ALLOW_HEADERS     = "Access-Control-Allow-Headers";
    private static final String  REQUEST_HEADER_ORIGIN            = "Origin";
    private static final String  ALLOW_METHOD                     = "GET, POST, PUT, DELETE, OPTIONS, HEAD";
    private static final String  ALLOW_HEADERS                    = "accept, Content-Type, X-CustomToken, Authorization, Access-Control-Allow-Origin";

    public CorsHeaderFilter(){
        super();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String origin = request.getHeader(REQUEST_HEADER_ORIGIN);

        // CORS 가능하도록 응답 헤더 추가
        if (StringUtils.hasLength(origin))
        {
            // 요청한 도메인에 대해 CORS 를 허용한다. 제한이 필요하다면 필요한 값으로 설정한다.
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);

            // credentials 허용
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

            // 허용 methods
            response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, ALLOW_METHOD);

            // 허용 headers
            response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, ALLOW_HEADERS);

        }
        filterChain.doFilter(request, response);
    }




}
