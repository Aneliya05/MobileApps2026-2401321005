package bg.uni.plovdiv.fmi.medassist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Brand Colors
val EmergencyRedDark = Color(0xFFB71C1C)
val EmergencyRedLight = Color(0xFFE53935)

// Light Theme Palette
val LightScreenBackground = Color(0xFFF8F9FA)
val LightSurface = Color(0xFFFFFFFF)
val LightTextPrimary = Color(0xFF1A1A1A)
val LightTextSecondary = Color(0xFF666666)
val LightSoftPinkBanner = Color(0xFFFDECEC)
val LightCardBorder = Color(0xFFEEEEEE)
val LightIconBg = Color(0xFFF0F0F0)

// Dark Theme Palette
val DarkScreenBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkTextPrimary = Color(0xFFF0F0F0)
val DarkTextSecondary = Color(0xFFA0A0A0)
val DarkSoftPinkBanner = Color(0xFF2E1A1A)
val DarkCardBorder = Color(0xFF2C2C2C)
val DarkIconBg = Color(0xFF2C2C2C)

// Dynamic Theme-aware Colors
val ScreenBackground: Color
    @Composable
    get() = MaterialTheme.colorScheme.background

val TextPrimary: Color
    @Composable
    get() = MaterialTheme.colorScheme.onBackground

val TextSecondary: Color
    @Composable
    get() = MaterialTheme.colorScheme.onSurfaceVariant

val SoftPinkBanner: Color
    @Composable
    get() = MaterialTheme.colorScheme.surfaceVariant

val CardBorderGray: Color
    @Composable
    get() = MaterialTheme.colorScheme.outline

val IconLightGrayBg: Color
    @Composable
    get() = if (MaterialTheme.colorScheme.background == DarkScreenBackground) DarkIconBg else LightIconBg
