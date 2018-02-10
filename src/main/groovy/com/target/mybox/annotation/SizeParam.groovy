package com.target.mybox.annotation

import groovy.transform.AnnotationCollector
import org.springframework.web.bind.annotation.RequestParam

@AnnotationCollector
@RequestParam(required = false, defaultValue = '5')
@interface SizeParam {
}
