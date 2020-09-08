package com.fusemachine.util;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        final String expired = (String) request.getAttribute("expired");
        if (expired != null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, expired);
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid User Login Details.");
        }
    }

    @Override
    public void setRealmName(String realmName) {
        super.setRealmName(realmName);
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("fuse_canteen");
        super.afterPropertiesSet();
    }
}
