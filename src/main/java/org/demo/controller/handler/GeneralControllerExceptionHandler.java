package org.demo.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.demo.controller.handler.json.HttpErrorInfoJson;
import org.demo.exception.ServiceException;
import org.demo.utils.FormatUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GeneralControllerExceptionHandler {

    @ExceptionHandler(value = {BadCredentialsException.class})
    @ResponseBody
    public ResponseEntity<HttpErrorInfoJson> handleBadCredentialsException(BadCredentialsException badCredentialsException, HttpServletRequest request) {
        HttpErrorInfoJson httpErrorInfoJson = FormatUtils.httpErrorInfoFormatted(HttpStatus.UNAUTHORIZED, request, badCredentialsException);
        log.error(httpErrorInfoJson.toString());
        log.error(Arrays.toString(badCredentialsException.getStackTrace()));
        return new ResponseEntity<>(httpErrorInfoJson, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ServiceException.class})
    @ResponseBody
    public ResponseEntity<HttpErrorInfoJson> handleServiceException(ServiceException serviceException, HttpServletRequest request) {
        HttpErrorInfoJson httpErrorInfoJson;

        switch (serviceException.getCode()) {
            case 400 -> {
                httpErrorInfoJson = FormatUtils.httpErrorInfoFormatted(HttpStatus.BAD_REQUEST, request, serviceException);
                log.error(httpErrorInfoJson.toString());
                log.error(Arrays.toString(serviceException.getStackTrace()));
                return new ResponseEntity<>(httpErrorInfoJson, HttpStatus.BAD_REQUEST);
            }
            case 404 -> {
                httpErrorInfoJson = FormatUtils.httpErrorInfoFormatted(HttpStatus.NOT_FOUND, request, serviceException);
                log.error(httpErrorInfoJson.toString());
                log.error(Arrays.toString(serviceException.getStackTrace()));
                return new ResponseEntity<>(httpErrorInfoJson, HttpStatus.NOT_FOUND);
            }
            default -> {
                httpErrorInfoJson = FormatUtils.httpErrorInfoFormatted(HttpStatus.INTERNAL_SERVER_ERROR, request, serviceException);
                log.error(httpErrorInfoJson.toString());
                log.error(Arrays.toString(serviceException.getStackTrace()));
                return new ResponseEntity<>(httpErrorInfoJson, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
