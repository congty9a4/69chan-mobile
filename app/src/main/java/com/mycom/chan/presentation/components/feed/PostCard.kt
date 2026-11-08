package com.mycom.chan.presentation.components.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mycom.chan.domain.model.Post
import com.mycom.chan.presentation.theme.Spacing

@Composable
fun PostCard(
    post: Post,
    onLike: () -> Unit = {},
    onComment: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        PostHeader(
            avatarUrl = post.user.avatarUrl,
            userName = post.user.userName,
            modifier = Modifier.padding(horizontal = Spacing.lg)
        )
        PostContent(
            postStatus = post.caption.ifBlank { null },
            postContent = post.medias.firstOrNull()?.url,
            modifier = Modifier.padding(horizontal = Spacing.lg)
        )
        // Each action button has 8dp inner padding, so 8dp here lines the first icon up
        // with the avatar at 16dp. Their 48dp touch area also gives the bottom spacing.
        PostBar(
            likes = post.likes,
            comments = post.comment,
            isLiked = post.isLiked,
            onLike = onLike,
            onComment = onComment,
            modifier = Modifier.padding(horizontal = Spacing.sm)
        )
    }
}
