package com.mycom.chan

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.mycom.chan.ui.navigation.AppNav
import com.mycom.chan.ui.theme.ChanTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import io.sentry.Sentry

@HiltAndroidApp
class Main : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    // waiting for view to draw to better represent a captured error with a screenshot
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
                val navController = rememberNavController()
                AppNav(navController = navController)
            }
        }
    }
}
