package com.mycom.chan.presentation.components.navbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mycom.chan.R

@Composable
fun TopNavBar (onMenuClick:() -> Unit) {
    Box(
        modifier = Modifier.run {
            fillMaxWidth()
                .height(90.dp)
                .padding(vertical = 2.dp, horizontal = 10.dp)
                .statusBarsPadding()

        }
    ) {
        NavigationButton(
            R.drawable.menu,
            contentDescription = "menu",
            Modifier.size(30.dp).align(Alignment.CenterStart),
            30,onMenuClick
        )
        NavigationButton(
            R.drawable.logo,
            contentDescription = "logo",
            Modifier.size(30.dp).align(Alignment.Center),
            30, {}
        )

    }
}

