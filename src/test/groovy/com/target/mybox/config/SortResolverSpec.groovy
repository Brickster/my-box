package com.target.mybox.config

import com.target.mybox.exception.SortFormatException
import org.springframework.core.MethodParameter
import org.springframework.data.domain.Sort
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import spock.lang.Specification
import spock.lang.Unroll

class SortResolverSpec extends Specification {

  SortResolver sortResolver = new SortResolver()

  MethodParameter methodParameter = Mock(MethodParameter)
  ModelAndViewContainer modelAndViewContainer = Mock(ModelAndViewContainer)
  NativeWebRequest nativeWebRequest = Mock(NativeWebRequest)
  WebDataBinderFactory webDataBinderFactory = Mock(WebDataBinderFactory)

  @Unroll
  void 'resolve sort with #description'() {

    when:
    Sort actualSort = sortResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory)

    then:
    1 * nativeWebRequest.getParameter('sort') >> sort
    0 * _

    actualSort == expectedSort

    where:
    sort        | expectedSort                          | description
    null        | null                                  | 'null'
    ''          | null                                  | 'a blank string'
    ' '         | null                                  | 'an empty string'
    'name:asc'  | new Sort(Sort.Direction.ASC, 'name')  | 'name:asc'
    'name:desc' | new Sort(Sort.Direction.DESC, 'name') | 'name:desc'
    'NAme:desc' | new Sort(Sort.Direction.DESC, 'NAme') | 'NAme:desc'
  }

  @Unroll
  void 'resolve sort throws exception for bad sort format #sort'() {

    given:

    when:
    sortResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory)

    then:
    1 * nativeWebRequest.getParameter('sort') >> sort
    0 * _

    thrown(SortFormatException)

    where:
    sort             | _
    'name:ASC'       | _
    'name'           | _
    'name:a'         | _
    'name:ascending' | _
    'name(asc)'      | _
    '+name'          | _
  }
}
