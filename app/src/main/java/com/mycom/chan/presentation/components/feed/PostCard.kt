package com.mycom.chan.presentation.components.feed


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mycom.chan.domain.model.Post

@Composable
fun PostCard(
    post: Post,
    onLike: () -> Unit = {},
    onComment: () -> Unit = {}
) {
    Column (modifier = Modifier
        .fillMaxWidth()
//        .height(300.dp)
        .padding(
            vertical = 5.dp,
            horizontal = 5.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        PostHeader(
            avatarUrl = post.user.avatarUrl,
            userName = post.user.userName
        )
        PostContent(
            postStatus = post.caption.ifBlank { null },
            postContent = post.medias.firstOrNull()?.url
        )
        PostBar(
            likes = post.likes,
            comments = post.comment,
            isLiked = post.isLiked,
            onLike = onLike,
            onComment = onComment
        )
        }

}
