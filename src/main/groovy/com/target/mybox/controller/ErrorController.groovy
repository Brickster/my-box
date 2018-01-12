package com.target.mybox.controller

import com.target.mybox.exception.MyBoxException
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController
@ControllerAdvice
class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

  @ExceptionHandler([MyBoxException])
  ResponseEntity<ErrorResponse> handleMyBoxException(MyBoxException e) {
    ResponseStatus responseStatus = e.class.getAnnotation(ResponseStatus)
    if (responseStatus) {
      return new ResponseEntity<ErrorResponse>(
          new ErrorResponse(error: e.getClass().getSimpleName(), message: responseStatus.reason()),
          responseStatus.code()
      )
    }
    return handleUnknownException(e)
  }

  @ExceptionHandler([HttpRequestMethodNotSupportedException])
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  ErrorResponse handleUnsupportedRequestMethod(HttpRequestMethodNotSupportedException e) {
    return new ErrorResponse(error: e.getClass().getSimpleName(), message: 'Method not supported')
  }

  @RequestMapping(value = '/error')
  @ExceptionHandler([Exception])
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  ErrorResponse handleUnknownException(Exception e) {
    log.error("unknown error occurred: ${e.getClass().getSimpleName()}", e)
    new ErrorResponse(error: 'InternalServerErrorException', message: 'An unknown error occurred')
  }

  @Override
  String getErrorPath() {
    return '/error'
  }

  static class ErrorResponse {
    String error
    String message
  }
}
