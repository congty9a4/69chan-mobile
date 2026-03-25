package com.mycom.chan.presentation.page

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.mycom.chan.presentation.components.Menu
import com.mycom.chan.presentation.components.navbar.TopNavBar
import com.mycom.chan.presentation.components.navbar.BottomNavBar
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun Feed() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet() {
                Menu()
            }
        }
    ) {
        Scaffold (
            topBar = { TopNavBar(
                onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
                     },
            bottomBar = { BottomNavBar() }
        ) {
                innerPadding ->
        }
    }
}