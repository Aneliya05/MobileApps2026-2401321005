package bg.uni.plovdiv.fmi.medassist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = EmergencyRedLight,
    secondary = EmergencyRedDark,
    background = DarkScreenBackground,
    surface = DarkSurface,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkCardBorder,
    surfaceVariant = DarkSoftPinkBanner
)

private val LightColorScheme = lightColorScheme(
    primary = EmergencyRedLight,
    secondary = EmergencyRedDark,
    background = LightScreenBackground,
    surface = LightSurface,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary,
    onSurfaceVariant = LightTextSecondary,
    outline = LightCardBorder,
    surfaceVariant = LightSoftPinkBanner
)

@Composable
fun MedAssistTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
