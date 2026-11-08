package com.mycom.chan.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.unit.sp

// Material 3 type scale. Screens pick a role (titleMedium, bodyMedium...) instead of a raw sp
// value, so text scales consistently and follows the user's font size setting.
//
// The only change from the M3 defaults: labelSmall goes from 11sp to 12sp,
// so no text in the app is smaller than 12sp.
private val Default = Typography()

val Typography = Typography(
    labelSmall = Default.labelSmall.copy(fontSize = 12.sp, lineHeight = 16.sp),
)
