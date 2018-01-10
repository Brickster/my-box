package com.target.mybox.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = 'Page must be positive')
class PageMustBePositiveException extends RuntimeException {
}
