package com.mycom.chan.presentation.components.feed

import android.icu.text.CompactDecimalFormat
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.R
import java.util.Locale

enum class ActionType{
    LIKE,
    COMMENT,
    SHARE,
    BOOKMARK
}

@Composable
fun PostBar(
    likes: Int=0,
    comments: Int=0,
    isLiked: Boolean= false,
    onLike:() ->Unit = {},
    onComment: () ->Unit = {}
) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        ActionButton(ActionType.LIKE, count = likes, isActive = isLiked, onClick = onLike)
        ActionButton(ActionType.COMMENT, count = comments, onClick = onComment)
        ActionButton(ActionType.SHARE)
        ActionButton(ActionType.BOOKMARK)
    }
}

@Composable
fun ActionButton(type: ActionType,
                 count: Int? = null,
                 isActive: Boolean = false,
                 onClick: () -> Unit = {}
) {
    val format = remember {
        CompactDecimalFormat.getInstance(
            Locale.US,
            CompactDecimalFormat.CompactStyle.SHORT
        )
    }

    val icon: ImageVector = when (type) {
        ActionType.LIKE -> {
            if(isActive) {
                ImageVector.vectorResource(id = R.drawable.heart_filled)
            }
            else ImageVector.vectorResource(id = R.drawable.heart_blank)
        }
        ActionType.COMMENT -> {
            ImageVector.vectorResource(id = R.drawable.comment)
        }
        ActionType.SHARE -> {
            ImageVector.vectorResource(id = R.drawable.shared)
        }
        ActionType.BOOKMARK -> {
            ImageVector.vectorResource(id = R.drawable.bookmark)
        }
    }
    Row (verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable(
            onClick = onClick
        )) {
        Icon(imageVector = icon,
            contentDescription = "action-icon",
            modifier = Modifier.size(15.dp)
        )

        if(count!=null) {
            Text(
                text = format.format(count  ),
                fontSize = 12.sp,
                lineHeight = 12.sp
            )
        }
    }
}


