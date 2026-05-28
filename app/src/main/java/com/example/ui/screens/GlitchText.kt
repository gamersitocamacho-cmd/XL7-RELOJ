package com.example.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.delay

@Composable
fun GlitchText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.White,
    glitchDurationMs: Long = 1800,
    prefix: String = ""
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        val glitchChars = "XL7#$[]_<>@%&*10"
        val cycles = 8
        for (step in 0..text.length) {
            val targetPart = text.substring(0, step)
            for (cycle in 0 until cycles) {
                val sb = StringBuilder(targetPart)
                for (rem in step until text.length) {
                    if (text[rem] == ' ') {
                        sb.append(' ')
                    } else {
                        sb.append(glitchChars.random())
                    }
                }
                displayedText = prefix + sb.toString()
                delay(glitchDurationMs / (text.length * cycles).coerceAtLeast(1))
            }
        }
        displayedText = prefix + text
    }

    Text(
        text = displayedText,
        modifier = modifier,
        style = style,
        color = color,
        fontFamily = FontFamily.Monospace
    )
}
