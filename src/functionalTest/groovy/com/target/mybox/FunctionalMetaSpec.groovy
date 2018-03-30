package com.target.mybox

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(classes = [Application], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = ['faulty=true', 'server.port=8081'])
@ContextConfiguration(classes = [Application])
class FunctionalMetaSpec extends BaseFunctionalSpec {
}
