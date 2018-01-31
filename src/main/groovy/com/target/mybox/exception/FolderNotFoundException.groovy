package com.target.mybox.exception

import groovy.transform.CompileStatic
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@CompileStatic
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = 'Folder not found')
class FolderNotFoundException extends MyBoxException {
}
