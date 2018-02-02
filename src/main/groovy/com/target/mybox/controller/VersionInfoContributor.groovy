package com.target.mybox.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.info.Info
import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.stereotype.Component

@Component
class VersionInfoContributor implements InfoContributor {

  @Value('${api.version}')
  String version

  @Override
  void contribute(Info.Builder builder) {
    builder.withDetail('version', version)
  }
}
