package com.mycom.chan

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.mycom.chan.presentation.navigation.AppNav
import com.mycom.chan.presentation.theme.ChanTheme
import com.mycom.chan.presentation.viewmodel.AuthViewModel
import com.mycom.chan.presentation.viewmodel.LocalAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import io.sentry.Sentry

@HiltAndroidApp
class Main : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<android.view.View>(android.R.id.content).viewTreeObserver.addOnGlobalLayoutListener {
            try {
                throw Exception("This app uses Sentry! :)")
            } catch (e: Exception) {
                Sentry.captureException(e)
            }
        }
        enableEdgeToEdge()
        setContent {
            ChanTheme {
                CompositionLocalProvider(LocalAuthViewModel provides authViewModel) {
                    val navController = rememberNavController()
                    AppNav(navController = navController)
                }
            }
        }
    }
}
