package com.target.mybox.controller

import org.springframework.boot.actuate.info.Info
import spock.lang.Specification

class VersionInfoContributorSpec extends Specification {

  VersionInfoContributor versionInfoContributor = new VersionInfoContributor(
      version: '1.0.0'
  )

  void 'contribute'() {

    given:
    Info.Builder builder = Mock(Info.Builder)

    when:
    versionInfoContributor.contribute(builder)

    then:
    1 * builder.withDetail('version', versionInfoContributor.version)
    0 * _
  }
}
