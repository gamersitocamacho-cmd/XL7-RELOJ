package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TerminalViewModel
import kotlinx.coroutines.delay

@Composable
fun ClockFacade(
    viewModel: TerminalViewModel,
    modifier: Modifier = Modifier
) {
    val timeStr by viewModel.timeString.collectAsState()
    val dateStr by viewModel.dateString.collectAsState()
    val inputCode by viewModel.inputDigits.collectAsState()

    var showKeypad by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "clockRotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Animated Particle Background
            ParticleBackground()

            // Cyber Grid Background Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val gridColor = Color.White.copy(alpha = 0.03f)
                val strokeWidth = 1f

                // Draw vertical grid lines
                val stepX = size.width / 16f
                for (i in 0..16) {
                    val x = i * stepX
                    drawLine(gridColor, start = androidx.compose.ui.geometry.Offset(x, 0f), end = androidx.compose.ui.geometry.Offset(x, size.height), strokeWidth = strokeWidth)
                }

                // Draw horizontal grid lines
                val stepY = size.height / 32f
                for (i in 0..32) {
                    val y = i * stepY
                    drawLine(gridColor, start = androidx.compose.ui.geometry.Offset(0f, y), end = androidx.compose.ui.geometry.Offset(size.width, y), strokeWidth = strokeWidth)
                }

                // Cyber compass outer circle
                val centerOffset = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height * 0.28f)
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = 350f,
                    center = centerOffset,
                    style = Stroke(width = 2f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f))
                )
            }

            // Top Status Panel (Futuristic UI details)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "STATUS: SAFE_GUARD",
                    color = Color.White.copy(alpha = 0.4f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                )
                Text(
                    text = "X7_KERNEL_V1",
                    color = Color.White.copy(alpha = 0.4f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                )
            }

            // Main central Clock content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large elegant, thin digital clock display
                Text(
                    text = timeStr,
                    color = Color.White,
                    fontSize = 62.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp,
                    modifier = Modifier
                        .clickable { showKeypad = !showKeypad }
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Date label
                Text(
                    text = dateStr,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )

                val phrase by viewModel.greetingPhrase.collectAsState()

                Spacer(modifier = Modifier.height(18.dp))

                // Ambient Automatic Greeting Phrase with Glitch Reveal
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.03f))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        GlitchText(
                            text = phrase,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            ),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Interactive Access Trigger
                Button(
                    onClick = { showKeypad = !showKeypad },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showKeypad) Color.White else Color.Transparent,
                        contentColor = if (showKeypad) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                        .height(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Access Panel Keys",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (showKeypad) "CERRAR PUENTE" else "INICIAR SESIÓN",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    )
                }
            }

            // Slide up tactical biometric / keyboard interface
            AnimatedVisibility(
                visible = showKeypad,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Black, RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TERMINAL DE REGISTRO CLAVE",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input display box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(55.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.04f))
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (idx in 0 until 3) {
                            val active = inputCode.length > idx
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .size(20.dp, 4.dp)
                                    .background(if (active) Color.White else Color.White.copy(alpha = 0.15f))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Custom key grid
                    val keypadKeys = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("CLEAR", "0", "LOCK")
                    )

                    for (row in keypadKeys) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (key in row) {
                                KeypadButton(
                                    label = key,
                                    onClick = {
                                        when (key) {
                                            "CLEAR" -> viewModel.clearClockInput()
                                            "LOCK" -> {
                                                showKeypad = false
                                                viewModel.clearClockInput()
                                            }
                                            else -> viewModel.onClockKeyPress(key)
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "NOTA: EL ACCESO SECRETO REQUIERE LA SINCRONIZACIÓN '7:77'",
                        color = Color.White.copy(alpha = 0.35f),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun KeypadButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAction = label == "CLEAR" || label == "LOCK"
    val contentColor = if (isAction) Color.White.copy(alpha = 0.7f) else Color.White
    val textStyle = if (isAction) {
        TextStyle(fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
    } else {
        TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Light)
    }

    Box(
        modifier = modifier
            .size(72.dp, 48.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .clickable { onClick() }
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = contentColor,
            fontFamily = FontFamily.Monospace,
            style = textStyle,
            textAlign = TextAlign.Center
        )
    }
}
