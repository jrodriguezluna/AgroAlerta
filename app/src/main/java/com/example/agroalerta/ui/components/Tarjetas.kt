package com.example.agroalerta.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agroalerta.ui.theme.BordeSuave

/** Tarjeta base: fondo blanco con borde suave. Si recibe [onClick] es tocable. */
@Composable
fun AgroCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    container: Color = Color.White,
    borde: Color = BordeSuave,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = container,
            border = BorderStroke(1.dp, borde),
            content = content
        )
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = container,
            border = BorderStroke(1.dp, borde),
            content = content
        )
    }
}

/** Caja con ícono, título y detalle (modo lectura, nota para técnico, alerta revisada...). */
@Composable
fun CajaInformativa(
    icono: ImageVector,
    titulo: String,
    detalle: String?,
    modifier: Modifier = Modifier,
    fondo: Color = Color(0xFFEDF3EA),
    colorTexto: Color = Color(0xFF38523B)
) {
    Surface(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = fondo) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icono, contentDescription = null, tint = colorTexto, modifier = Modifier.size(22.dp))
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(titulo, color = colorTexto, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                if (detalle != null) Text(detalle, color = colorTexto, fontSize = 12.sp)
            }
        }
    }
}
