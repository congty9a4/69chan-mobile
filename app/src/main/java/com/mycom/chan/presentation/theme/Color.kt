package com.mycom.chan.presentation.theme

import androidx.compose.ui.graphics.Color

// Brand palette. Screens must not use these directly: go through
// MaterialTheme.colorScheme or ChanTheme.colors so light and dark mode both work.
//
// Primary blue #1D61E7 gives 5.4:1 contrast with white text (WCAG AA needs 4.5:1).
// The old #4C9EEB only reached 2.8:1 with white, so it is used in dark mode instead.

// ---- Light scheme ----
internal val BrandBlue = Color(0xFF1D61E7)
internal val OnBrandBlue = Color(0xFFFFFFFF)
internal val BrandBlueContainer = Color(0xFFDBE5FF)
internal val OnBrandBlueContainer = Color(0xFF00174A)

internal val SlateSecondary = Color(0xFF545F71)
internal val OnSlateSecondary = Color(0xFFFFFFFF)
internal val SlateSecondaryContainer = Color(0xFFD8E3F8)
internal val OnSlateSecondaryContainer = Color(0xFF111C2B)

internal val VioletTertiary = Color(0xFF7B4E9C)
internal val OnVioletTertiary = Color(0xFFFFFFFF)
internal val VioletTertiaryContainer = Color(0xFFF3DAFF)
internal val OnVioletTertiaryContainer = Color(0xFF2F004C)

internal val ErrorLight = Color(0xFFBA1A1A)
internal val OnErrorLight = Color(0xFFFFFFFF)
internal val ErrorContainerLight = Color(0xFFFFDAD6)
internal val OnErrorContainerLight = Color(0xFF410002)

internal val SurfaceLight = Color(0xFFF9F9FF)
internal val OnSurfaceLight = Color(0xFF191C20)
internal val SurfaceVariantLight = Color(0xFFE0E2EC)
internal val OnSurfaceVariantLight = Color(0xFF44474F)
internal val OutlineLight = Color(0xFF74777F)
internal val OutlineVariantLight = Color(0xFFC4C6D0)
internal val SurfaceContainerLowestLight = Color(0xFFFFFFFF)
internal val SurfaceContainerLowLight = Color(0xFFF3F3FA)
internal val SurfaceContainerLight = Color(0xFFEDEDF4)
internal val SurfaceContainerHighLight = Color(0xFFE7E8EE)
internal val SurfaceContainerHighestLight = Color(0xFFE2E2E9)

// ---- Dark scheme ----
internal val BrandBlueDark = Color(0xFFADC6FF)
internal val OnBrandBlueDark = Color(0xFF002E69)
internal val BrandBlueContainerDark = Color(0xFF004494)
internal val OnBrandBlueContainerDark = Color(0xFFD8E2FF)

internal val SlateSecondaryDark = Color(0xFFBCC7DC)
internal val OnSlateSecondaryDark = Color(0xFF263141)
internal val SlateSecondaryContainerDark = Color(0xFF3C4758)
internal val OnSlateSecondaryContainerDark = Color(0xFFD8E3F8)

internal val VioletTertiaryDark = Color(0xFFE2B6FF)
internal val OnVioletTertiaryDark = Color(0xFF47206A)
internal val VioletTertiaryContainerDark = Color(0xFF603782)
internal val OnVioletTertiaryContainerDark = Color(0xFFF3DAFF)

internal val ErrorDark = Color(0xFFFFB4AB)
internal val OnErrorDark = Color(0xFF690005)
internal val ErrorContainerDark = Color(0xFF93000A)
internal val OnErrorContainerDark = Color(0xFFFFDAD6)

internal val SurfaceDark = Color(0xFF111318)
internal val OnSurfaceDark = Color(0xFFE2E2E9)
internal val SurfaceVariantDark = Color(0xFF44474F)
internal val OnSurfaceVariantDark = Color(0xFFC4C6D0)
internal val OutlineDark = Color(0xFF8E9099)
internal val OutlineVariantDark = Color(0xFF44474F)
internal val SurfaceContainerLowestDark = Color(0xFF0C0E13)
internal val SurfaceContainerLowDark = Color(0xFF191C20)
internal val SurfaceContainerDark = Color(0xFF1D2024)
internal val SurfaceContainerHighDark = Color(0xFF282A2F)
internal val SurfaceContainerHighestDark = Color(0xFF33353A)

// ---- Brand background for the auth screens (Login, Register, Verify, Create profile) ----
internal val AuthGradientLight = listOf(Color(0xFFC3FBE8), Color(0xFFE8C3FB), Color(0xFFFBE8C3))
internal val AuthGradientDark = listOf(Color(0xFF0E2B24), Color(0xFF2A1736), Color(0xFF2E2614))

// ---- Notification types. Each one passes 4.5:1 on its own 12% tint. ----
internal val LikeLight = Color(0xFFC62828)
internal val CommentLight = Color(0xFF1565C0)
internal val FriendLight = Color(0xFF2E7D32)
internal val AcceptLight = Color(0xFF8E24AA)

internal val LikeDark = Color(0xFFFF8A80)
internal val CommentDark = Color(0xFF90CAF9)
internal val FriendDark = Color(0xFFA5D6A7)
internal val AcceptDark = Color(0xFFCE93D8)
