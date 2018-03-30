package com.target.mybox.config

import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import org.springframework.core.MethodParameter
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import spock.lang.Specification
import spock.lang.Unroll

@SuppressWarnings('EmptyMethod')
class PageableResolverSpec extends Specification {

  PageableResolver pageableResolver = new PageableResolver(new SortResolver())

  MethodParameter methodParameter = Mock(MethodParameter)
  ModelAndViewContainer modelAndViewContainer = Mock(ModelAndViewContainer)
  NativeWebRequest nativeWebRequest = Mock(NativeWebRequest)
  WebDataBinderFactory webDataBinderFactory = Mock(WebDataBinderFactory)

  @Unroll
  void 'resolve pageable from page #page and size #size'() {

    when:
    Pageable pageable = pageableResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory)

    then:
    (1.._) * methodParameter.getMethod() >> PageableResolverSpec.getMethod('getDocuments')
    (1.._) * nativeWebRequest.getParameter('page') >> page
    (1.._) * nativeWebRequest.getParameter('size') >> size

    pageable.pageNumber == page
    pageable.pageSize == size

    where:
    page | size
    0    | 10
  }

  @Unroll
  void 'resolve pageable throws exception for method #methodName and bad page value #page'() {

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
    -1   | 'getFolders'
    -2   | 'getFolders'
    -10  | 'getFolders'
  }

  @Unroll
  void 'resolve pageable throws exception for bad size value #size'() {

    when:
    pageableResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory)

    then:
    1 * methodParameter.getMethod() >> PageableResolverSpec.getMethod(methodName)
    1 * nativeWebRequest.getParameter('page') >> 0
    1 * nativeWebRequest.getParameter('size') >> size
    0 * _

    thrown(SizeMustBePositiveException)

    where:
    size | methodName
    0    | 'getDocuments'
    -1   | 'getDocuments'
    -2   | 'getDocuments'
    -10  | 'getDocuments'
    0    | 'getFolders'
  }

  // used to get around not being able to mock Method
  void getFolders() { }
  void getDocuments() { }
}
