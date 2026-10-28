package com.example.agroalerta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.ui.theme.BordeSuave
import com.example.agroalerta.ui.theme.FondoApp
import com.example.agroalerta.ui.theme.VerdePrimario
import com.example.agroalerta.ui.theme.VerdeTexto

/**
 * Barra superior de la app. Si recibe [onAtras] muestra la flecha; si no, el logo.
 * Si recibe [rol] muestra el badge de rol, y si recibe [onPerfil] el avatar con iniciales.
 */
@Composable
fun AgroTopBar(
    titulo: String,
    modifier: Modifier = Modifier,
    rol: Rol? = null,
    iniciales: String? = null,
    onAtras: (() -> Unit)? = null,
    onPerfil: (() -> Unit)? = null
) {
    Column(modifier.background(FondoApp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            if (onAtras != null) {
                IconButton(onClick = onAtras) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver")
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(VerdePrimario),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Eco, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Text(
                text = titulo,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (rol != null) {
                val forma = RoundedCornerShape(8.dp)
                Text(
                    text = if (rol == Rol.SUPERVISOR) "Supervisor" else "Técnico",
                    modifier = Modifier
                        .clip(forma)
                        .background(Color(0xFFE5F2E2))
                        .border(1.dp, Color(0xFFA6CDA7), forma)
                        .padding(horizontal = 7.dp, vertical = 4.dp),
                    color = Color(0xFF175E25),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (onPerfil != null && iniciales != null) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD6EAD5))
                        .clickable(onClick = onPerfil)
                        .semantics { contentDescription = "Abrir perfil" },
                    contentAlignment = Alignment.Center
                ) {
                    Text(iniciales, color = VerdeTexto, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
        HorizontalDivider(color = BordeSuave)
    }
}

/** Franja ámbar que avisa que se están mostrando las últimas lecturas guardadas (sin conexión). */
@Composable
fun StaleBanner(modifier: Modifier = Modifier, compacto: Boolean = false, hace: String = "hace 25 min") {
    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFEDBD))
                .heightIn(min = if (compacto) 46.dp else 56.dp)
                .padding(horizontal = 16.dp, vertical = if (compacto) 5.dp else 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Rounded.CloudOff, contentDescription = null, tint = Color(0xFF6B4A00))
            Column {
                Text("Mostrando últimas lecturas", color = Color(0xFF6B4A00), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(hace, color = Color(0xFF6B4A00), fontSize = 11.sp)
            }
        }
        HorizontalDivider(color = Color(0xFFE7C96E))
    }
}

@Composable
fun EncabezadoSeccion(titulo: String, modifier: Modifier = Modifier, detalle: String? = null) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 2.dp, end = 2.dp, top = 20.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(titulo, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        if (detalle != null) {
            Text(detalle, fontSize = 11.sp, color = Color(0xFF697369))
        }
    }
}
