package com.mycom.chan.ui.components.post

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
@Preview
fun PostBar() {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        ActionButton(ActionType.LIKE, 1000000000)
        ActionButton(ActionType.COMMENT, 20)
        ActionButton(ActionType.SHARE)
        ActionButton(ActionType.BOOKMARK)
    }
}

@Composable
fun ActionButton(type: ActionType,
                 count: Int?=null) {
    val format = remember {
        CompactDecimalFormat.getInstance(
            Locale.US,
            CompactDecimalFormat.CompactStyle.SHORT
        )
    }

    val icon: ImageVector = when (type) {
        ActionType.LIKE -> {
            ImageVector.vectorResource(id = R.drawable.heart_blank)
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
            onClick = {}
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


