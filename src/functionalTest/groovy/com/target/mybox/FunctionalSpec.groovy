package com.target.mybox

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(classes = [Application], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = ['faulty=false'])
@ContextConfiguration(classes = [Application])
class FunctionalSpec extends BaseFunctionalSpec {
}
