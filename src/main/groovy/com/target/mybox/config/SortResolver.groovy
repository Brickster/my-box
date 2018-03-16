package com.target.mybox.config

import com.target.mybox.exception.SortFormatException
import org.springframework.core.MethodParameter
import org.springframework.data.domain.Sort
import org.springframework.data.web.SortArgumentResolver
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer

class SortResolver implements SortArgumentResolver {

  private static final String SORT_FORMAT = /\w+:(asc|desc)/

  @Override
  boolean supportsParameter(MethodParameter parameter) {
    return Sort == parameter.getParameterType()
  }

  @Override
  Sort resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory
  ) {

    String sort = webRequest.getParameter('sort')
    if (!sort?.trim()) {
      return null
    }
    if (!(sort ==~ SORT_FORMAT)) {
      throw new SortFormatException()
    }

    String[] parts = sort.split(':')
    Sort.Direction direction = parts[1] == 'asc' ? Sort.Direction.ASC : Sort.Direction.DESC
    return new Sort(direction, parts[0])
  }
}
