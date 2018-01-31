package com.target.mybox.exception

import groovy.transform.CompileStatic
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@CompileStatic
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = 'Page must be positive')
class PageMustBePositiveException extends MyBoxException {
}
