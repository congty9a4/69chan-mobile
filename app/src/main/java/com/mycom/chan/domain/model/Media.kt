package com.mycom.chan.domain.model

import java.time.Instant

data class Media  (
    val url: String,
    val type: String,
    val uploadedAt: Instant
)