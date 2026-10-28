package com.example.agroalerta.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Solo tema claro y sin colores dinámicos: la app se usa en terreno con luz solar y el diseño
 * de Figma es claro, así que la paleta de marca debe verse igual en todos los teléfonos.
 */
private val EsquemaClaro = lightColorScheme(
    primary = VerdePrimario,
    onPrimary = Color.White,
    primaryContainer = VerdeContenedor,
    onPrimaryContainer = Color(0xFF0F3D17),
    secondary = Color(0xFF526350),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCEFD9),
    onSecondaryContainer = VerdeTexto,
    background = FondoApp,
    onBackground = TextoPrincipal,
    surface = FondoApp,
    onSurface = TextoPrincipal,
    surfaceVariant = Color(0xFFE5F2E2),
    onSurfaceVariant = Color(0xFF586257),
    surfaceContainer = FondoNav,
    outline = BordeCampo,
    outlineVariant = BordeSuave,
    error = RojoCritico,
    onError = Color.White,
    errorContainer = Color(0xFFFCE3E2),
    onErrorContainer = Color(0xFFA01F25)
)

@Composable
fun AgroAlertaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}
