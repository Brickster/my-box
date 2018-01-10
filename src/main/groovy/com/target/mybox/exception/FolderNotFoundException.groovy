package com.target.mybox.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = 'Folder not found')
class FolderNotFoundException extends RuntimeException {
}
