package com.mycom.chan.presentation.components.login_register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.mycom.chan.presentation.theme.ChanTheme
import com.mycom.chan.presentation.theme.MaxFormWidth
import com.mycom.chan.presentation.theme.MotionDuration
import com.mycom.chan.presentation.theme.Spacing

/**
 * Shared layout for Login, Register, Verify email and Create profile: brand gradient,
 * a centered card capped at [MaxFormWidth], scrolling when the keyboard is open,
 * and the snackbar at the bottom of the screen (not inside the card, where it pushed
 * the content around).
 *
 * [enterFromStart] picks the slide direction, so going Login -> Register feels like moving sideways.
 */
@Composable
fun AuthScreenContainer(
    snackbarHostState: SnackbarHostState,
    enterFromStart: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(ChanTheme.colors.authGradient))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.lg, vertical = Spacing.xl),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(MotionDuration.MEDIUM)) +
                    slideInHorizontally(tween(MotionDuration.MEDIUM, easing = FastOutSlowInEasing)) { width ->
                        if (enterFromStart) -width / 8 else width / 8
                    }
            ) {
                Card(
                    // widthIn before fillMaxWidth, otherwise the cap is ignored
                    modifier = Modifier
                        .widthIn(max = MaxFormWidth)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        // Mostly opaque so text stays readable over the gradient in both themes
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.92f),
                        // Must be explicit: with the alpha above, Material cannot match the
                        // container to a scheme color and falls back to black text, unreadable in dark mode
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.xl),
                        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = content
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .imePadding()
                .padding(Spacing.lg)
        )
    }
}
