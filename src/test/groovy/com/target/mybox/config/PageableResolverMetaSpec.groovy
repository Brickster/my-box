package com.target.mybox.config

import com.target.mybox.exception.PageMustNotBeNegativeException
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import spock.lang.Specification
import spock.lang.Unroll

@SuppressWarnings('EmptyMethod')
class PageableResolverMetaSpec extends Specification {

  PageableResolver pageableResolver = new PageableResolver(new SortResolver(), true)

  MethodParameter methodParameter = Mock(MethodParameter)
  ModelAndViewContainer modelAndViewContainer = Mock(ModelAndViewContainer)
  NativeWebRequest nativeWebRequest = Mock(NativeWebRequest)
  WebDataBinderFactory webDataBinderFactory = Mock(WebDataBinderFactory)

  @Unroll
  void 'resolve pageable throws exception for bad page value #page'() {

    when:
    pageableResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory)

    then:
    1 * methodParameter.getMethod() >> PageableResolverSpec.getMethod(methodName)
    1 * nativeWebRequest.getParameter('page') >> page
    1 * nativeWebRequest.getParameter('size') >> 10
    0 * _

    thrown(PageMustNotBeNegativeException)

    where:
    page | methodName
    -1   | 'getDocuments'
    -2   | 'getDocuments'
    -10  | 'getDocuments'

    // getFolders is intentionally backwards and throws exceptions for positive numbers
    1    | 'getFolders'
    2    | 'getFolders'
    10   | 'getFolders'
  }

  // used to get around not being able to mock Method
  void getFolders() { }
  void getDocuments() { }
}
