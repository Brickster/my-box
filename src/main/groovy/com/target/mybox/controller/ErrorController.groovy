package com.target.mybox.controller

import com.target.mybox.exception.MyBoxException
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Slf4j
@CompileStatic
@RestController
@ControllerAdvice
class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

  String errorPath = '/error'

  @ExceptionHandler([MyBoxException])
  ResponseEntity<ErrorResponse> handleMyBoxException(MyBoxException e) {
    ResponseStatus responseStatus = e.class.getAnnotation(ResponseStatus)
    if (responseStatus) {
      return new ResponseEntity<ErrorResponse>(
          new ErrorResponse(error: e.getClass().getSimpleName(), message: responseStatus.reason()),
          responseStatus.code()
      )
    }
    return new ResponseEntity<ErrorResponse>(handleUnknownException(e), HttpStatus.INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler([MethodArgumentNotValidException])
  ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

    if (e.bindingResult.getFieldErrorCount()) {
      String message = e.bindingResult.getFieldErrors().collect { "${it.field} ${it.defaultMessage}" }.join('; ')
      ErrorResponse errorResponse = new ErrorResponse(
          error: MethodArgumentNotValidException.getSimpleName(),
          message: message
      )
      return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST)
    }
    return new ResponseEntity<ErrorResponse>(handleUnknownException(e), HttpStatus.INTERNAL_SERVER_ERROR)
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

  static class ErrorResponse {
    String error
    String message
  }
}
