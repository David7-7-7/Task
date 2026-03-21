package com.taskeando.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskeando.ui.theme.*

// ── Datos de ejemplo ──────────────────────
data class EventoUI(
    val hora: String,
    val titulo: String,
    val subtitulo: String = "",
    val tipo: String   // "Tarea" | "Llamada" | "Default"
)

private val eventosEjemplo = listOf(
    EventoUI("8:00",  "Tarea 1",           tipo = "Tarea"),
    EventoUI("9:00",  "Reunión de equipo", subtitulo = "1 hora · Zoom", tipo = "Default"),
    EventoUI("10:00", "Llamar a Alex",     subtitulo = "Pendiente",     tipo = "Llamada"),
    EventoUI("Luego", "Tarea 2",           tipo = "Tarea"),
)

// Días con tareas marcadas (para puntos indicadores)
private val diasConTareas = setOf(4, 6, 11)
private const val HOY = 3

// Grid de Marzo 2025: empieza en sábado → 4 nulls de relleno al inicio
private val diasMarzo: List<Int?> = listOf(
    null, null, null, null, null, 1, 2,
    3, 4, 5, 6, 7, 8, 9,
    10, 11, 12, 13, 14, 15, 16,
    17, 18, 19, 20, 21, 22, 23,
    24, 25, 26, 27, 28, 29, 30,
    31, null, null, null, null, null, null
)

// ─────────────────────────────────────────
// PANTALLA 4 — Calendario
// ─────────────────────────────────────────
@Composable
fun CalendarioScreen(
    onVerTareas: () -> Unit,
    onNuevaTarea: () -> Unit
) {
    var vistaSeleccionada by remember { mutableStateOf("Mes") }
    var diaSeleccionado   by remember { mutableStateOf(HOY) }
    var mesActual         by remember { mutableStateOf("Marzo 2025") }

    Scaffold(
        containerColor = WarmWhite,
        bottomBar = {
            TaskeandoBottomBar(
                tabActivo = "calendario",
                onTareasClick = onVerTareas,
                onCalendarioClick = {},
                onFabClick = onNuevaTarea
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // ── Header mes
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconoBtn(
                        icon = Icons.Default.ArrowBack,
                        descripcion = "Mes anterior",
                        onClick = { mesActual = "Febrero 2025" }
                    )
                    Text(
                        text = mesActual,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    IconoBtn(
                        icon = Icons.Default.ArrowForward,
                        descripcion = "Mes siguiente",
                        onClick = { mesActual = "Abril 2025" }
                    )
                }
            }

            // ── Toggle vista
            item {
                Row(
                    modifier = Modifier.padding(start = 22.dp, top = 4.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Día", "Mes", "Semana").forEach { vista ->
                        ChipVista(
                            texto = vista,
                            activo = vistaSeleccionada == vista,
                            onClick = { vistaSeleccionada = vista }
                        )
                    }
                }
            }

            // ── Cabecera L M M J V S D
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    listOf("L", "M", "M", "J", "V", "S", "D").forEach { d ->
                        Text(
                            text = d,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextLight
                        )
                    }
                }
            }

            // ── Grid del mes
            item {
                GridCalendario(
                    dias = diasMarzo,
                    diaSeleccionado = diaSeleccionado,
                    diasConTareas = diasConTareas,
                    hoy = HOY,
                    onDiaClick = { diaSeleccionado = it }
                )
            }

            // ── Timeline
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Hoy · Lunes $diaSeleccionado",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 4.dp)
                )
            }

            items(eventosEjemplo) { evento ->
                EventoTimelineItem(evento = evento)
            }
        }
    }
}

// ── Grid visual del mes ───────────────────

@Composable
fun GridCalendario(
    dias: List<Int?>,
    diaSeleccionado: Int,
    diasConTareas: Set<Int>,
    hoy: Int,
    onDiaClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        dias.chunked(7).forEach { semana ->
            Row(modifier = Modifier.fillMaxWidth()) {
                semana.forEach { dia ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dia != null) {
                            val esHoy      = dia == hoy
                            val esSel      = dia == diaSeleccionado && !esHoy
                            val tieneTask  = dia in diasConTareas

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            esHoy -> Accent
                                            esSel -> AccentLight
                                            else  -> Color.Transparent
                                        }
                                    )
                                    .clickable { onDiaClick(dia) }
                            ) {
                                Text(
                                    text = "$dia",
                                    fontSize = 12.sp,
                                    fontWeight = if (esHoy || esSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (esHoy) Color.White else TextDark
                                )
                            }

                            // Punto indicador
                            if (tieneTask && !esHoy) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .offset(y = (-4).dp)
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(Accent)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Item de timeline ──────────────────────

@Composable
fun EventoTimelineItem(evento: EventoUI) {
    val (bgColor, titleColor) = when (evento.tipo) {
        "Tarea"   -> AccentLight to PurpleDeep
        "Llamada" -> OrangeLight to Color(0xFF7A3D1A)
        else      -> Stone       to TextDark
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = evento.hora,
            fontSize = 11.sp,
            color = TextLight,
            modifier = Modifier.width(42.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(bgColor)
                .padding(10.dp)
        ) {
            Text(text = evento.titulo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = titleColor)
            if (evento.subtitulo.isNotEmpty()) {
                Text(
                    text = evento.subtitulo,
                    fontSize = 10.sp,
                    color = if (evento.tipo == "Llamada") Orange else TextLight
                )
            }
        }
    }
}