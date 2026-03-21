package com.taskeando.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskeando.ui.theme.*
import com.taskeando.ui.theme.Accent
import com.taskeando.ui.theme.AccentLight
import com.taskeando.ui.theme.Cream

// ─────────────────────────────────────────
// PANTALLA 1 — Splash / Bienvenida
// ─────────────────────────────────────────
@Composable
fun SplashScreen(
    onEmpezar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        // Decoración circular fondo
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 40.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentLight, Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo placeholder (reemplaza con tu imagen real)
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(AccentLight),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", fontSize = 48.sp, color = Accent)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Taskeando",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleDeep,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Organiza tu día con claridad\ny sin estrés",
                fontSize = 13.sp,
                color = TextLight,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onEmpezar,
                modifier = Modifier.height(52.dp),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = "Empezar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }
}