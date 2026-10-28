package com.example.agroalerta.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta tomada del diseño de Figma
val VerdePrimario = Color(0xFF2E7D32)
val VerdeTexto = Color(0xFF195F29)
val VerdeOscuro = Color(0xFF164F21)
val VerdeContenedor = Color(0xFFCCE8CA)
val FondoApp = Color(0xFFF8FAF4)
val FondoNav = Color(0xFFF0F5EC)
val TextoPrincipal = Color(0xFF1A1C19)
val TextoSecundario = Color(0xFF697269)
val BordeSuave = Color(0xFFE1E7DD)
val BordeCampo = Color(0xFFAEB9AD)
val RojoCritico = Color(0xFFC62828)

/**
 * Colores de un estado (sensor, equipo, alerta). Material 3 no tiene "ámbar" ni "estado" en su
 * esquema, así que se definen aparte. Nunca se usa solo el color: siempre va con ícono + texto.
 */
data class ColoresEstado(
    val texto: Color,
    val fondo: Color,
    val suave: Color = fondo,
    val borde: Color = fondo
)

private fun estado(texto: Long, fondo: Long, suave: Long = fondo, borde: Long = fondo) =
    ColoresEstado(Color(texto), Color(fondo), Color(suave), Color(borde))

object ColoresAgro {
    val normal = estado(0xFF1F6B2D, 0xFFE1F2DF, 0xFFEEF7EC, 0xFFD5E8D2)
    val precaucion = estado(0xFF795500, 0xFFFFF0C9, 0xFFFFF8E3, 0xFFF0DDA6)
    val critico = estado(0xFFA01F25, 0xFFFCE3E2, 0xFFFFF4F2, 0xFFF2D0CD)
    val sinDatos = estado(0xFF59625A, 0xFFE8EBE7, 0xFFF1F3F0, 0xFFDDE1DC)
    val apagado = estado(0xFF5E695F, 0xFFEDF0EB)
    val mantencion = estado(0xFF456173, 0xFFE0EBF1)

    // Estado de gestión de alertas
    val nueva = estado(0xFF175D86, 0xFFDCEEF8)
    val revisada = estado(0xFF24652E, 0xFFDFF0DD)
    val resuelta = estado(0xFF5A625B, 0xFFE7EAE6)
}
