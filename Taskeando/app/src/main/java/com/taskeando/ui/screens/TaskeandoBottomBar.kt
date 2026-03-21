package com.taskeando.ui.screens



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskeando.ui.theme.*

@Composable
fun TaskeandoBottomBar(
    tabActivo: String,
    onTareasClick: () -> Unit,
    onCalendarioClick: () -> Unit,
    onFabClick: () -> Unit
) {
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Tareas",
                color = if (tabActivo == "tareas") Accent else TextLight,
                modifier = Modifier.clickable { onTareasClick() }
            )

            Spacer(modifier = Modifier.width(40.dp))

            Text(
                text = "Calendario",
                color = if (tabActivo == "calendario") Accent else TextLight,
                modifier = Modifier.clickable { onCalendarioClick() }
            )
        }

        // FAB centro
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
                .size(56.dp)
                .background(Accent, CircleShape)
                .clickable { onFabClick() },
            contentAlignment = Alignment.Center
        ) {
            Text("+", color = Color.White, fontSize = 24.sp)
        }
    }
}