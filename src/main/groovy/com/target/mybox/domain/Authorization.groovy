package com.target.mybox.domain

import com.target.mybox.annotation.JsonIsoFormat
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate

import java.time.Instant

class Authorization {
  @Id
  String id
  String notes
  String token
  String hashedToken

  @JsonIsoFormat
  @CreatedDate
  Instant created
  @JsonIsoFormat
  @LastModifiedDate
  Instant lastModified
}
