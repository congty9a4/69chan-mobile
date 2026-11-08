package com.mycom.chan.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Spacing on a 4/8dp grid. Use these instead of one-off values such as 5.dp or 10.dp. */
object Spacing {
    val xxs = 2.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
}

object IconSize {
    val small = 20.dp
    val medium = 24.dp
}

object AvatarSize {
    val small = 36.dp
    val medium = 40.dp
    val large = 48.dp
    val profile = 88.dp
}

/** Animation durations in ms, shared so screens move at the same pace. */
object MotionDuration {
    const val SHORT = 150
    const val MEDIUM = 300
}

/** Content width cap for forms and cards, so they do not stretch edge to edge on tablets. */
val MaxFormWidth = 420.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
)
