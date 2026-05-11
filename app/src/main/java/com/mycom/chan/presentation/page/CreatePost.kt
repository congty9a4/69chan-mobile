package com.mycom.chan.presentation.page

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mycom.chan.domain.model.Media
import com.mycom.chan.domain.model.User
import com.mycom.chan.presentation.components.create_post.BottomCreatePostBar
import com.mycom.chan.presentation.components.create_post.CreatePostContent
import com.mycom.chan.presentation.components.create_post.TopCreatePostBar

@Composable
fun CreatePost(
    onCancelPost: () -> Unit,
    onCreatePost: () -> Unit,
    isPostAble: Boolean,
    user: User,
    onAddImage: () -> Unit,
    onAddVideo: () -> Unit,
    text: String,
    onTextChange: (String) -> Unit,
    selectedUris: List<Uri>
) {
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .systemBarsPadding(),
        topBar = {
            TopCreatePostBar(
                onCancelPost = onCancelPost,
                onCreatePost = onCreatePost,
                isPostAble = isPostAble,
                user = user
            )
        },
        bottomBar = {
            BottomCreatePostBar(
                onAddImage = onAddImage,
                onAddVideo = onAddVideo
            )
        }
    ) { innerPadding->
        CreatePostContent(
            text = text,
            onTextChange = onTextChange,
            selectedUris =  selectedUris,
            modifier = Modifier.padding(innerPadding)
        )


    }
}