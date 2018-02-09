package com.target.mybox.annotation

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.AnnotationCollector

/**
 * Spring's out of the box Instant formatter drops millisecond precision when it's zero. This pattern causes
 * 2016-01-02T03:04:05.000Z to print as 2016-01-02T03:04:05.000Z rather than 2016-01-02T03:04:05Z.
 */
@AnnotationCollector
@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = 'UTC')
class JsonIsoFormat {
}
