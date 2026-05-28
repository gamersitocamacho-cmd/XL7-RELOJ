package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TerminalViewModel
import kotlinx.coroutines.delay

@Composable
fun UnlockSequence(
    viewModel: TerminalViewModel,
    modifier: Modifier = Modifier
) {
    val progress by viewModel.unlockProgress.collectAsState()
    val msg by viewModel.unlockStageMessage.collectAsState()
    val stage by viewModel.unlockSequenceState.collectAsState() // "BLACK_OUT", "LOADING", "GLITCH_WELCOME"

    val infiniteTransition = rememberInfiniteTransition(label = "GlitchPulsing")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorBlink"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Drifting Tech Particles
        ParticleBackground()

        when (stage) {
            "BLACK_OUT" -> {
                // Completely pure black screen for heavy suspension
                Box(modifier = Modifier.fillMaxSize())
            }
            "LOADING" -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Scanning Radar Rings
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(160.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White,
                            strokeWidth = 3.dp,
                            trackColor = Color.White.copy(alpha = 0.08f)
                        )

                        Text(
                            text = "${(progress * 100).toInt()}%",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    // Diagnostic Logs Output
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.03f))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "▉  ",
                            color = Color.White.copy(alpha = cursorAlpha),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                        Text(
                            text = msg.uppercase(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 2.sp,
                            maxLines = 1
                        )
                    }
                }
            }
            "GLITCH_WELCOME" -> {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "CONEXIÓN ESTABLECIDA",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        letterSpacing = 4.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Large glowing glitch welcome text
                    GlitchText(
                        text = "Bienvenido a Casas XL7",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            textAlign = TextAlign.Center
                        ),
                        color = Color.White,
                        glitchDurationMs = 1500
                    )

                    val phrase by viewModel.greetingPhrase.collectAsState()
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "「 $phrase 」",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "INICIANDO TERMINAL PROTOCOLO PRV_7...",
                        color = Color.White.copy(alpha = 0.35f),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    )
                }
            }
        }
    }
}
