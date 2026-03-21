package com.taskeando.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskeando.ui.theme.*

// ── Modelo de datos local (sin BD) ───────
data class TareaUI(
    val id: Int,
    val titulo: String,
    val hora: String,
    val tipo: String,           // "Tarea" | "Llamada" | "Evento"
    val estado: String,         // "Pendiente" | "Completada"
    val esDeHoy: Boolean = true
)

// Datos de ejemplo estáticos
private val tareasEjemplo = listOf(
    TareaUI(1, "Reunión con el equipo", "9:00 am",  "Evento",  "Completada", esDeHoy = true),
    TareaUI(2, "Llamar a Alex",         "10:00 am", "Llamada", "Pendiente",  esDeHoy = true),
    TareaUI(3, "Revisar informe",       "3:00 pm",  "Tarea",   "Pendiente",  esDeHoy = false),
)

// ─────────────────────────────────────────
// PANTALLA 2 — Mis Tareas
// ─────────────────────────────────────────
@Composable
fun MisTareasScreen(
    onNuevaTarea: () -> Unit,
    onVerCalendario: () -> Unit,
    onVerDetalle: (TareaUI) -> Unit
) {
    var vistaSeleccionada by remember { mutableStateOf("Día") }
    val tareasHoy       = tareasEjemplo.filter { it.esDeHoy }
    val tareasPendientes = tareasEjemplo.filter { !it.esDeHoy }

    Scaffold(
        containerColor = WarmWhite,
        bottomBar = {
            TaskeandoBottomBar(
                tabActivo = "tareas",
                onTareasClick = {},
                onCalendarioClick = onVerCalendario,
                onFabClick = onNuevaTarea
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            // ── Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mis Tareas",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconoBtn(icon = Icons.Default.Search, descripcion = "Buscar")
                        IconoBtn(icon = Icons.Default.Settings, descripcion = "Ajustes")
                    }
                }
            }

            // ── Toggle Día / Semana / Mes
            item {
                Row(
                    modifier = Modifier.padding(start = 22.dp, bottom = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Día", "Semana", "Mes").forEach { vista ->
                        ChipVista(
                            texto = vista,
                            activo = vistaSeleccionada == vista,
                            onClick = { vistaSeleccionada = vista }
                        )
                    }
                }
            }

            // ── Fecha
            item {
                Text(
                    text = "Lunes, 3 de Marzo 2025",
                    fontSize = 12.sp,
                    color = TextLight,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 22.dp, bottom = 16.dp)
                )
            }

            // ── Sección Hoy
            item {
                SeccionLabel(
                    texto = "HOY · ${tareasHoy.size} PENDIENTES",
                    modifier = Modifier.padding(start = 22.dp, bottom = 10.dp)
                )
            }

            items(tareasHoy.size) { i ->
                TareaCard(tarea = tareasHoy[i], onClick = { onVerDetalle(tareasHoy[i]) })
            }

            // ── Sección Pendientes
            item {
                SeccionLabel(
                    texto = "PENDIENTES",
                    modifier = Modifier.padding(start = 22.dp, top = 8.dp, bottom = 10.dp)
                )
            }

            if (tareasPendientes.isEmpty()) {
                item { EmptyState() }
            } else {
                items(tareasPendientes.size) { i ->
                    TareaCard(tarea = tareasPendientes[i], onClick = { onVerDetalle(tareasPendientes[i]) })
                }
            }
        }
    }
}

// ── Composables reutilizables ─────────────

@Composable
fun TareaCard(tarea: TareaUI, onClick: () -> Unit) {
    val completada = tarea.estado == "Completada"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Checkbox visual
            if (completada) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Green),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = Color.White, fontSize = 10.sp)
                }
            } else {
                Surface(
                    modifier = Modifier.size(20.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Transparent,
                    border = BorderStroke(2.dp, Stone)
                ) {}
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarea.titulo,
                    fontSize = 13.sp,
                    fontWeight = if (completada) FontWeight.Normal else FontWeight.Medium,
                    color = if (completada) Muted else TextDark,
                    textDecoration = if (completada) TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ChipEtiqueta(texto = tarea.hora, color = TextLight, bg = WarmWhite)
                    when (tarea.tipo) {
                        "Llamada" -> ChipEtiqueta("Llamada", Orange, OrangeLight)
                        "Evento"  -> ChipEtiqueta("Evento",  Green,  GreenLight)
                        else      -> ChipEtiqueta("Tarea",   Accent,  AccentLight)
                    }
                }
            }
            Text("···", color = Muted, fontSize = 16.sp)
        }
    }
}

@Composable
fun ChipVista(texto: String, activo: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(if (activo) Accent else Stone)
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (activo) Color.White else TextLight
        )
    }
}

@Composable
fun ChipEtiqueta(texto: String, color: Color, bg: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(text = texto, fontSize = 10.sp, color = color)
    }
}

@Composable
fun SeccionLabel(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = Muted,
        modifier = modifier
    )
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("☁️", fontSize = 32.sp)
        Text(
            text = "No hay tareas pendientes.\n¡Buen trabajo!",
            fontSize = 13.sp,
            color = TextLight,
            lineHeight = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun IconoBtn(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    descripcion: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Stone)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = descripcion,
            modifier = Modifier.size(16.dp),
            tint = TextDark
        )
    }
}