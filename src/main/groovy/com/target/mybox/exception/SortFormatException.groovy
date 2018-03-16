package com.target.mybox.exception

import groovy.transform.CompileStatic
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@CompileStatic
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = /sort must have format \d+:(asc|desc)/)
class SortFormatException extends MyBoxException {
}
