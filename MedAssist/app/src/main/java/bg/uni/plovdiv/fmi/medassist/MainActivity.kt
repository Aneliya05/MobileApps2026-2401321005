package bg.uni.plovdiv.fmi.medassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import bg.uni.plovdiv.fmi.medassist.navigation.AppNavigation
import bg.uni.plovdiv.fmi.medassist.ui.theme.MedAssistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDark = isSystemInDarkTheme()
            var isDarkMode by rememberSaveable { mutableStateOf(systemDark) }

            MedAssistTheme(darkTheme = isDarkMode) {
                AppNavigation(
                    isDarkMode = isDarkMode,
                    onDarkModeChanged = { isDarkMode = it }
                )
            }
        }
    }
}
