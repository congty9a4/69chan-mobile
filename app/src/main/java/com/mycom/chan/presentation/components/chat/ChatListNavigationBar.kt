package com.mycom.chan.presentation.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.R
import com.mycom.chan.presentation.uimodel.ChatFilter
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
@Composable
fun ChatListNavigationBar(
    onBackToMainPage: () -> Unit,
    modifier: Modifier = Modifier,
    currentFilter: ChatFilter,
    onFilterSelected: (ChatFilter) -> Unit,
    onNewContact: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = Color(0xFF4a4E57),
        tonalElevation = 4.dp // Thêm một chút bóng để phân biệt với nội dung
        ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = onBackToMainPage
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.back),
                    contentDescription = "Back to main page",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
            FilterToggle(
                currentFilter = currentFilter,
                onFilterSelected = onFilterSelected
            )
            IconButton(
                onClick = onNewContact
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        R.drawable.add_chat,
                    ),
                    contentDescription = "add-new-contact",
                    tint = Color.White
                )
            }

        }
    }
}

@Composable
fun FilterToggle(
    currentFilter: ChatFilter,
    onFilterSelected: (ChatFilter) -> Unit
) {
    val options = listOf(ChatFilter.ALL, ChatFilter.UNREAD)
    val labels = listOf("All", "Unread")
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, filter ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { onFilterSelected(filter) },
                selected = currentFilter == filter,
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color.White,
                    activeContentColor = Color.Black,
                    inactiveContainerColor = Color.Transparent,
                    inactiveContentColor = Color.LightGray,
                    activeBorderColor = Color.LightGray,
                    inactiveBorderColor = Color.LightGray
                )
            ) {
                Text(
                    text = labels[index],
                    fontWeight = if (currentFilter == filter) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun FilterItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(shape =  RoundedCornerShape(4.dp))
            // Nếu được chọn thì nền Trắng, không thì Trong suốt để lộ màu xám của Row
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp),

        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            // Chữ được chọn thì màu Đen đậm, không chọn thì màu Xám
            color = if (isSelected) Color.Black else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Preview
    (showBackground = true, backgroundColor = 0xFF2C2F33) @Composable fun ChatListNavigationBarPreview() { ChatListNavigationBar(
    onBackToMainPage = {},
    modifier = Modifier,
    currentFilter = ChatFilter.ALL,
    onFilterSelected = {},
        onNewContact = {}
) }