package com.target.mybox.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = 'Document not found')
class DocumentNotFoundException extends MyBoxException {
}
