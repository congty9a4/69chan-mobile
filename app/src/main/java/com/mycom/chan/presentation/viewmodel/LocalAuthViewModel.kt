package com.mycom.chan.presentation.viewmodel

import androidx.compose.runtime.staticCompositionLocalOf
import com.mycom.chan.presentation.viewmodel.AuthViewModel

val LocalAuthViewModel = staticCompositionLocalOf<AuthViewModel> {
    error("LocalAuthViewModel not provided")
}