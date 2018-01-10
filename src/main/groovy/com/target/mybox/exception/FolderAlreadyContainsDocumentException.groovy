package com.target.mybox.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = 'Folder already contains document')
class FolderAlreadyContainsDocumentException extends RuntimeException {
}
