package com.mycom.chan.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = OnBrandBlue,
    primaryContainer = BrandBlueContainer,
    onPrimaryContainer = OnBrandBlueContainer,
    secondary = SlateSecondary,
    onSecondary = OnSlateSecondary,
    secondaryContainer = SlateSecondaryContainer,
    onSecondaryContainer = OnSlateSecondaryContainer,
    tertiary = VioletTertiary,
    onTertiary = OnVioletTertiary,
    tertiaryContainer = VioletTertiaryContainer,
    onTertiaryContainer = OnVioletTertiaryContainer,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = SurfaceLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandBlueDark,
    onPrimary = OnBrandBlueDark,
    primaryContainer = BrandBlueContainerDark,
    onPrimaryContainer = OnBrandBlueContainerDark,
    secondary = SlateSecondaryDark,
    onSecondary = OnSlateSecondaryDark,
    secondaryContainer = SlateSecondaryContainerDark,
    onSecondaryContainer = OnSlateSecondaryContainerDark,
    tertiary = VioletTertiaryDark,
    onTertiary = OnVioletTertiaryDark,
    tertiaryContainer = VioletTertiaryContainerDark,
    onTertiaryContainer = OnVioletTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = SurfaceDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,
)

/** Colors the Material 3 scheme has no slot for. Read them through [ChanTheme.colors]. */
@Immutable
data class ChanColors(
    val authGradient: List<Color>,
    val like: Color,
    val comment: Color,
    val friendRequest: Color,
    val friendAccepted: Color,
)

private val LightChanColors = ChanColors(
    authGradient = AuthGradientLight,
    like = LikeLight,
    comment = CommentLight,
    friendRequest = FriendLight,
    friendAccepted = AcceptLight,
)

private val DarkChanColors = ChanColors(
    authGradient = AuthGradientDark,
    like = LikeDark,
    comment = CommentDark,
    friendRequest = FriendDark,
    friendAccepted = AcceptDark,
)

private val LocalChanColors = staticCompositionLocalOf { LightChanColors }

object ChanTheme {
    val colors: ChanColors
        @Composable
        @ReadOnlyComposable
        get() = LocalChanColors.current
}

@Composable
fun ChanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Off by default: wallpaper-based colors would replace the brand blue, and the app's
    // identity (auth gradient, buttons, links) is built around that blue.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalChanColors provides if (darkTheme) DarkChanColors else LightChanColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
