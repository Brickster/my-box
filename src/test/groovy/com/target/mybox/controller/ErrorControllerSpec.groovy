package com.target.mybox.controller

import com.target.mybox.exception.MyBoxException
import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ResponseStatus
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

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

  void 'handle MethodArgumentNotValidException with field errors'() {

    given:
    List<FieldError> fieldErrors = [new FieldError('test', 'name', 'is required'), new FieldError('test', 'birth_date', 'must be in the past')]
    BindingResult bindingResult = Mock(BindingResult)
    MethodArgumentNotValidException exception = Mock(MethodArgumentNotValidException)

    when:
    ResponseEntity<ErrorController.ErrorResponse> errorResponse = errorController.handleMethodArgumentNotValidException(exception)

    then:
    2 * exception.bindingResult >> bindingResult
    1 * bindingResult.getFieldErrorCount() >> fieldErrors.size()
    1 * bindingResult.getFieldErrors() >> fieldErrors
    0 * _

    errorResponse.statusCode == HttpStatus.BAD_REQUEST
    errorResponse.body.error == MethodArgumentNotValidException.getSimpleName()
    errorResponse.body.message == 'name is required; birth_date must be in the past'
  }

  void 'handle MethodArgumentNotValidException with non-field errors'() {

    given:
    BindingResult bindingResult = Mock(BindingResult)
    MethodArgumentNotValidException exception = Mock(MethodArgumentNotValidException)

    when:
    ResponseEntity<ErrorController.ErrorResponse> errorResponse = errorController.handleMethodArgumentNotValidException(exception)

    then:
    1 * exception.bindingResult >> bindingResult
    1 * bindingResult.getFieldErrorCount() >> 0
    1 * exception.getMessage() >> 'unknown error'
    1 * exception.getStackTrace() >> []
    1 * exception.getCause() >> new UnsupportedOperationException()
    0 * _

    errorResponse.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
    errorResponse.body.error == 'InternalServerErrorException'
    errorResponse.body.message == 'An unknown error occurred'
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

  void 'handle ConstraintViolationException'() {

    given:
    ConstraintViolation<String> violation1 = Mock(ConstraintViolation)
    ConstraintViolation<String> violation2 = Mock(ConstraintViolation)
    ConstraintViolationException exception = new ConstraintViolationException([violation1, violation2] as Set)

    when:
    ErrorController.ErrorResponse errorResponse = errorController.handleConstraintViolationException(exception)

    then:
    1 * violation1.propertyPath >> PathImpl.createPathFromString('p1')
    1 * violation1.message >> 'must not be blank'
    1 * violation2.propertyPath >> PathImpl.createPathFromString('p2')
    1 * violation2.message >> 'must be positive'
    0 * _

    errorResponse.error == ConstraintViolationException.getSimpleName()
    errorResponse.message == 'p1 must not be blank; p2 must be positive' ||
        errorResponse.message == 'p2 must be positive; p1 must not be blank'
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
