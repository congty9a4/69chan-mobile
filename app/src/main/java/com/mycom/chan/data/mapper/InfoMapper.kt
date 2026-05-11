package com.mycom.chan.data.mapper

import com.mycom.chan.data.remote.dto.model.InfoDTO
import com.mycom.chan.domain.model.Info

fun InfoDTO.toInfo() = Info(
    id = id,
    username = username,
    avatarUrl = avatarUrl
)