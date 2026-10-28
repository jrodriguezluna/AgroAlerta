package com.example.agroalerta.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** Mini gráfico de tendencia (solo supervisor). Dibuja una línea con las últimas lecturas. */
@Composable
fun Sparkline(
    puntos: List<Float>,
    color: Color,
    modifier: Modifier = Modifier.size(width = 34.dp, height = 14.dp)
) {
    Canvas(modifier = modifier) {
        if (puntos.size < 2) return@Canvas
        val minimo = puntos.min()
        val rango = (puntos.max() - minimo).takeIf { it > 0f } ?: 1f
        val paso = size.width / (puntos.size - 1)
        val ruta = Path()
        puntos.forEachIndexed { i, valor ->
            val x = i * paso
            val y = size.height - ((valor - minimo) / rango) * size.height
            if (i == 0) ruta.moveTo(x, y) else ruta.lineTo(x, y)
        }
        drawPath(
            path = ruta,
            color = color,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}
