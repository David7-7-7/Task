package com.taskeando.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskeando.ui.theme.*

// ─────────────────────────────────────────
// PANTALLA 5 — Detalle de Tarea
// Recibe una TareaUI directamente (sin ViewModel)
// ─────────────────────────────────────────
@Composable
fun DetalleTareaScreen(
    tarea: TareaUI,
    onBack: () -> Unit,
    onEditar: () -> Unit,
    onVerTareas: () -> Unit,
    onVerCalendario: () -> Unit,
    onNuevaTarea: () -> Unit
) {
    // Estado local del estado de la tarea (marcar hecha, mover)
    var estadoActual by remember { mutableStateOf(tarea.estado) }
    var tipoActual   by remember { mutableStateOf(tarea.tipo) }

    Scaffold(
        containerColor = WarmWhite,
        bottomBar = {
            TaskeandoBottomBar(
                tabActivo = "tareas",
                onTareasClick = onVerTareas,
                onCalendarioClick = onVerCalendario,
                onFabClick = onNuevaTarea
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = TextDark)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Detalle",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.weight(1f))
                IconoBtn(
                    icon = Icons.Default.Edit,
                    descripcion = "Editar",
                    onClick = onEditar
                )
            }

            // ── Hero
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Cream)
                    .padding(horizontal = 22.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "TAREAS → HOY",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = Muted
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tarea.titulo,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    when (tipoActual) {
                        "Llamada" -> ChipEtiqueta("Llamada", Orange, OrangeLight)
                        "Evento"  -> ChipEtiqueta("Evento",  Green,  GreenLight)
                        else      -> ChipEtiqueta("Tarea",   Accent,  AccentLight)
                    }
                    ChipEtiqueta(
                        texto = estadoActual,
                        color = TextDark,
                        bg = Stone
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Meta grid 2×2
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetaCard(label = "Fecha",      valor = "10 de Feb, 2025", modifier = Modifier.weight(1f))
                    MetaCard(label = "Hora",       valor = tarea.hora,        modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetaCard(label = "Recurrencia", valor = "Sin repetir", modifier = Modifier.weight(1f))
                    MetaCard(label = "Categoría",   valor = "Personal",    modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Descripción
            Column(
                modifier = Modifier.padding(horizontal = 22.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SeccionLabel(texto = "DESCRIPCIÓN")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "Recordar preguntarle sobre el proyecto y coordinar la siguiente reunión del equipo.",
                        fontSize = 13.sp,
                        color = TextDark,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Mover a
            SeccionLabel(
                texto = "MOVER A",
                modifier = Modifier.padding(start = 22.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Marcar hecha
                Box(
                    modifier = Modifier
                        .weight(1.4f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(if (estadoActual == "Completada") Green else Accent)
                        .clickable {
                            estadoActual = if (estadoActual == "Completada") "Pendiente" else "Completada"
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (estadoActual == "Completada") "✓ Hecha" else "✓ Marcar hecha",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Mover a Tarea
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(if (tipoActual == "Tarea") AccentLight else Stone)
                        .clickable { tipoActual = "Tarea" },
                    contentAlignment = Alignment.Center
                ) {
                    Text("📅 Tarea", fontSize = 11.sp, color = TextDark)
                }

                // Mover a Llamada
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(if (tipoActual == "Llamada") OrangeLight else Stone)
                        .clickable { tipoActual = "Llamada" },
                    contentAlignment = Alignment.Center
                ) {
                    Text("📓 Llamada", fontSize = 11.sp, color = TextDark)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── Meta card ─────────────────────────────

@Composable
fun MetaCard(label: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = TextLight
            )
            Text(
                text = valor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
    }
}