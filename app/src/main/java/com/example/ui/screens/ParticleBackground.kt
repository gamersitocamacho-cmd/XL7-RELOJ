package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

// Represents a slow drifting star/particle
class Particle(
    var x: Float,
    var y: Float,
    val radius: Float,
    var alpha: Float,
    val speedY: Float,
    val sinOffset: Float
)

@Composable
fun ParticleBackground(modifier: Modifier = Modifier) {
    val particles = remember {
        List(25) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 1.5f + 0.8f,
                alpha = Random.nextFloat() * 0.25f + 0.05f,
                speedY = Random.nextFloat() * 0.002f + 0.0008f,
                sinOffset = Random.nextFloat() * 3.14f
            )
        }
    }

    var tick by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            tick++
            delay(33) // Robust 30 FPS execution
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        if (width == 0f || height == 0f) return@Canvas

        particles.forEach { p ->
            // High-performance stateless particle vector calculations
            val rawY = p.y - (tick * p.speedY)
            val finalY = ((rawY % 1.0f) + 1.0f) % 1.0f

            val drift = kotlin.math.sin(tick * 0.012f + p.sinOffset) * 0.03f
            val finalX = ((p.x + drift % 1.0f) + 1.0f) % 1.0f

            val twinkledAlpha = p.alpha * (0.5f + 0.5f * kotlin.math.sin(tick * 0.04f + p.sinOffset))

            drawCircle(
                color = Color.White.copy(alpha = twinkledAlpha.coerceIn(0f, 1f)),
                radius = p.radius.dp.toPx(),
                center = Offset(finalX * width, finalY * height)
            )
        }
    }
}
