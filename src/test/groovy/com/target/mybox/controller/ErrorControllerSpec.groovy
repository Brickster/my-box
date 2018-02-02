package com.target.mybox.controller

import com.target.mybox.exception.MyBoxException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ResponseStatus
import spock.lang.Specification
import spock.lang.Unroll

class ErrorControllerSpec extends Specification {

  ErrorController errorController = new ErrorController()

  @Unroll
  void 'getErrorPath'() {
    expect:
    '/error' == errorController.getErrorPath()
  }

  @Unroll
  void 'handleMyBoxException'() {

    when:
    ResponseEntity<ErrorController.ErrorResponse> errorResponse = errorController.handleMyBoxException(exception)

    then:
    errorResponse.body.error == error
    errorResponse.body.message == message
    errorResponse.statusCode == statusCode

    where:
    exception                                           | error                                                      | message                     | statusCode
    new ErrorControllerExceptionWithResponseStatus()    | ErrorControllerExceptionWithResponseStatus.getSimpleName() | 'reason'                    | HttpStatus.BAD_REQUEST
    new ErrorControllerExceptionWithoutResponseStatus() | 'InternalServerErrorException'                             | 'An unknown error occurred' | HttpStatus.INTERNAL_SERVER_ERROR
  }

  void 'handleUnsupportedRequestException'() {

    given:
    HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException('')

    when:
    ErrorController.ErrorResponse errorResponse = errorController.handleUnsupportedRequestMethod(exception)

    then:
    errorResponse.error == HttpRequestMethodNotSupportedException.getSimpleName()
    errorResponse.message == 'Method not supported'
  }

  void 'handleUnknownException'() {

    when:
    ErrorController.ErrorResponse errorResponse = errorController.handleUnknownException(new Exception())

    then:
    errorResponse.error == 'InternalServerErrorException'
    errorResponse.message == 'An unknown error occurred'
  }

  @ResponseStatus(reason = 'reason', code = HttpStatus.BAD_REQUEST)
  private static class ErrorControllerExceptionWithResponseStatus extends MyBoxException {

  }

  private static class ErrorControllerExceptionWithoutResponseStatus extends MyBoxException {

  }
}