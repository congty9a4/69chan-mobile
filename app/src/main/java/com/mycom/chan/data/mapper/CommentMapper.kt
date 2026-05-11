package com.mycom.chan.data.mapper

import com.mycom.chan.data.remote.dto.model.CommentDTO
import com.mycom.chan.data.remote.dto.response.CommentResponse
import com.mycom.chan.domain.model.Comment

fun CommentDTO.toComment(): Comment = Comment(
    id = this.id,
    content = this.text,
    user = this.user.toUser(),
    createdAt = this.createdAt
)

fun CommentResponse.toComment(): Comment = Comment(
    id = this.id,
    content = this.text,
    user = this.user.toUser(),
    createdAt = this.createdAt ?: ""
)
