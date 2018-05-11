package com.target.mybox.controller

import com.target.mybox.service.AuthorizationsService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
@CompileStatic
@Component
class TokenAuthenticationInterceptor extends HandlerInterceptorAdapter {

  private static final String HEADER_PREFIX = 'Bearer '

  @Autowired
  AuthorizationsService authorizationsService

  @Override
  boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String token = request.getHeader(HttpHeaders.AUTHORIZATION)
    token = token.startsWith(HEADER_PREFIX) ? token.drop(HEADER_PREFIX.length()) : null
    if (token) {
      return authorizationsService.getByToken(token) as boolean
    }

    log.warn('no authentication token provided')
    return false
  }
}
