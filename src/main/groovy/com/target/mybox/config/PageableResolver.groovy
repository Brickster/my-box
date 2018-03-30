package com.target.mybox.config

import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import org.springframework.core.MethodParameter
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.data.web.SortArgumentResolver
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer

import java.lang.reflect.Method

class PageableResolver extends PageableHandlerMethodArgumentResolver {

  final boolean faulty

  PageableResolver(SortArgumentResolver sortArgumentResolver, boolean faulty = false) {
    super(sortArgumentResolver)
    this.faulty = faulty
  }

  @Override
  Pageable resolveArgument(
      MethodParameter methodParameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory
  ) {

    Method method = methodParameter.getMethod()
    String methodName = method.name
    Closure<Boolean> pageValidator = { it >= 0 }
    if (faulty && methodName == 'getFolders') {
      pageValidator = { it <= 0 }  // NOTE: intentionally error on numbers greater than 0 rather than less than
    }

    Integer page = webRequest.getParameter('page')?.toInteger()
    Integer size = webRequest.getParameter('size')?.toInteger()
    if (page != null && !pageValidator.call(page)) {
      throw new PageMustNotBeNegativeException()
    }
    if (size != null && size < 1) {
      throw new SizeMustBePositiveException()
    }

    return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory)
  }
}
