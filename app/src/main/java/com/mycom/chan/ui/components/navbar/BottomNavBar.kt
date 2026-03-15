package com.mycom.chan.ui.components.navbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mycom.chan.R

@Composable
fun BottomNavBar () {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(84.dp)
        .absolutePadding(left = 2.dp, top = 2.dp, right = 2.dp, bottom = 10.dp)
        .clickable(onClick = {}),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically) {
        NavigationButton(R.drawable.home,"home", size = 25, onClick = {})
        NavigationButton(R.drawable.search,"search", size = 25, onClick = {})
        NavigationButton(R.drawable.notification,"notification",size = 25, onClick = {})
        NavigationButton(R.drawable.message,"message", size = 25, onClick = {})
    }
}

