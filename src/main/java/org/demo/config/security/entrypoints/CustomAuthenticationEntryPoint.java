package org.demo.config.security.entrypoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.demo.utils.FormatUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException {
        var mapper = new ObjectMapper();
        var httpErrorInfoJson = FormatUtils.httpErrorInfoFormatted(HttpStatus.FORBIDDEN, req, authException);
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpStatus.FORBIDDEN.value());
        res.getWriter().write(mapper.writeValueAsString(httpErrorInfoJson));

        log.error(httpErrorInfoJson.toString());
        log.error(authException.getMessage(), authException);
    }
}