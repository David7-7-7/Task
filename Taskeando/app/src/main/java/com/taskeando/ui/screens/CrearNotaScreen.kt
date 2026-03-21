package com.taskeando.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
// PANTALLA 3 — Crear Nota / Tarea
// ─────────────────────────────────────────
@Composable
fun CrearNotaScreen(
    onBack: () -> Unit,
    onGuardar: () -> Unit
) {
    // Estado local del formulario
    var titulo          by remember { mutableStateOf("") }
    var descripcion     by remember { mutableStateOf("") }
    var fecha           by remember { mutableStateOf("") }
    var hora            by remember { mutableStateOf("") }
    var recurrencia     by remember { mutableStateOf("") }
    var tipoSeleccionado by remember { mutableStateOf("Tarea") }

    val tipos = listOf("Tarea", "Llamada / Reunión", "Evento")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
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
                    text = "Nueva Nota",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.size(48.dp))
            }

            // ── Selector de tipo
            Row(
                modifier = Modifier.padding(start = 22.dp, end = 22.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                tipos.forEach { tipo ->
                    ChipTipo(
                        texto = tipo,
                        activo = tipoSeleccionado == tipo,
                        onClick = { tipoSeleccionado = tipo }
                    )
                }
            }

            // ── Campos
            Column(
                modifier = Modifier.padding(horizontal = 22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Título
                CampoFormulario(label = "Título") {
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        placeholder = { Text("Escribe un título...", color = Muted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = campoColores(),
                        singleLine = true
                    )
                }

                // Descripción
                CampoFormulario(label = "Descripción") {
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        placeholder = { Text("Agrega detalles...", color = Muted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().height(90.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = campoColores(),
                        maxLines = 4
                    )
                }

                // Fila Fecha + Hora
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CampoFormulario(label = "Fecha", modifier = Modifier.weight(1f)) {
                        CampoIcono(
                            texto = fecha.ifEmpty { "DD / MM / AA" },
                            icono = "📅",
                            vacio = fecha.isEmpty(),
                            onClick = { /* Abrir DatePicker */ }
                        )
                    }
                    CampoFormulario(label = "Hora", modifier = Modifier.weight(1f)) {
                        CampoIcono(
                            texto = hora.ifEmpty { "HH : MM" },
                            icono = "🕐",
                            vacio = hora.isEmpty(),
                            onClick = { /* Abrir TimePicker */ }
                        )
                    }
                }

                // Recurrencia
                CampoFormulario(label = "Recurrencia") {
                    CampoIcono(
                        texto = recurrencia.ifEmpty { "Sin repetición" },
                        icono = "▾",
                        vacio = recurrencia.isEmpty(),
                        onClick = { /* Abrir selector */ }
                    )
                }

                Text(
                    text = "Se despliega el calendario para guardar la fecha exacta.",
                    fontSize = 11.sp,
                    color = TextLight,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // ── Botón Guardar fijo abajo
        Button(
            onClick = onGuardar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 22.dp)
                .height(52.dp),
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Accent),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "Guardar nota  ✓",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// ── Composables auxiliares ────────────────

@Composable
fun ChipTipo(texto: String, activo: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(if (activo) Accent else Stone)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (activo) Color.White else TextLight
        )
    }
}

@Composable
fun CampoFormulario(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            color = TextLight
        )
        content()
    }
}

@Composable
fun CampoIcono(texto: String, icono: String, vacio: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.5.dp, Stone, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = texto, fontSize = 12.sp, color = if (vacio) Muted else TextDark)
        Text(text = icono, fontSize = 14.sp)
    }
}

@Composable
fun campoColores() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Accent,
    unfocusedBorderColor = Stone,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = Accent,
    focusedTextColor = TextDark,
    unfocusedTextColor = TextDark
)