package com.mycom.a69chan.components.post


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun Post() {
    Column (modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .padding(
            vertical = 5.dp,
            horizontal = 5.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        PostHeader()
        PostContent("This is Ireland","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqboc-cZE1C6ngw8bLC2EMWXTAW1zBtqp4Nw&s")
        PostBar()
        }

}
