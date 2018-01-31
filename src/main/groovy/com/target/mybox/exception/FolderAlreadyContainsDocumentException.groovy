package com.target.mybox.exception

import groovy.transform.CompileStatic
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@CompileStatic
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = 'Folder already contains document')
class FolderAlreadyContainsDocumentException extends MyBoxException {
}
