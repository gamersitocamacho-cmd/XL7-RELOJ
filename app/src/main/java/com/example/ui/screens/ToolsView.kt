package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ChatMessage
import com.example.TerminalViewModel
import com.example.ToolSystem
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ToolsView(
    viewModel: TerminalViewModel,
    modifier: Modifier = Modifier
) {
    val activeTool by viewModel.activeToolSystem.collectAsState()

    AnimatedContent(
        targetState = activeTool,
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                    slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        },
        label = "ToolTransition"
    ) { tool ->
        when (tool) {
            ToolSystem.MENU -> ToolsSelectionMenu(onSelect = { viewModel.selectTool(it) }, modifier = modifier)
            ToolSystem.AI_TERMINAL -> AiTerminalConsole(viewModel = viewModel, onBack = { viewModel.selectTool(ToolSystem.MENU) }, modifier = modifier)
            ToolSystem.BINARY_CODEC -> BinaryTransmitterScreen(onBack = { viewModel.selectTool(ToolSystem.MENU) }, modifier = modifier)
            ToolSystem.POMODORO -> PomodoroFocusScreen(viewModel = viewModel, onBack = { viewModel.selectTool(ToolSystem.MENU) }, modifier = modifier)
            ToolSystem.KEYGRID -> CryptoKeyGeneratorScreen(onBack = { viewModel.selectTool(ToolSystem.MENU) }, modifier = modifier)
        }
    }
}

@Composable
fun ToolsSelectionMenu(
    onSelect: (ToolSystem) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "SECTOR: LABORATORIO_Y_HERRAMIENTAS",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 2x2 grid cards
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ToolMenuCard(
                        title = "INTELIGENCIA ARTIFICIAL",
                        subtitle = "Protocolo de IA Oráculo XL7",
                        desc = "Canal cifrado directo con inteligencia en red para planificar disciplinas, consultar dudas y estructurar metas.",
                        symbol = "AI_CORE_7",
                        onClick = { onSelect(ToolSystem.AI_TERMINAL) },
                        modifier = Modifier.weight(1f)
                    )
                    ToolMenuCard(
                        title = "BINARY TRANSMITTER",
                        subtitle = "Traductor de Bits",
                        desc = "Codifica letras comunes en pulsos binarios secretos (01111000) o descodifica códigos filtrados de inmediato.",
                        symbol = "BIN_COM",
                        onClick = { onSelect(ToolSystem.BINARY_CODEC) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ToolMenuCard(
                        title = "TEMPORIZADOR FOCUS SINC",
                        subtitle = "Reloj de Foco Pomodoro",
                        desc = "Ciclos síncronos de 25 min con alarmas sintetizadas para forjar disciplina ininterrumpida frente a la pantalla.",
                        symbol = "POM_SFX",
                        onClick = { onSelect(ToolSystem.POMODORO) },
                        modifier = Modifier.weight(1f)
                    )
                    ToolMenuCard(
                        title = "KEYGRID GENERATOR",
                        subtitle = "Creador Criptográfico & Sensibilidad",
                        desc = "Genera firmas seguras o perfiles recomendados de sensibilidad táctica (DPI/milli-sens) para optimización Free Fire.",
                        symbol = "KEY_GEN",
                        onClick = { onSelect(ToolSystem.KEYGRID) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// Tool list card helper components

@Composable
fun ToolMenuCard(
    title: String,
    subtitle: String,
    desc: String,
    symbol: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .border(1.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.02f))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = symbol,
                            color = Color.White,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = ":: COMP_O",
                        color = Color.White.copy(alpha = 0.3f),
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = subtitle.uppercase(),
                    color = Color.White.copy(alpha = 0.45f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Text(
                text = desc,
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun AiTerminalConsole(
    viewModel: TerminalViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chatMessages by viewModel.chatUiMessages.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    var textInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "TERMINAL: ORÁCULO_XL7_IA",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { viewModel.clearAiChat() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Wipe chat LOG", tint = Color.White.copy(alpha = 0.5f))
                }
            }

            // Scrolling terminal logs
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.02f))
                    .padding(12.dp)
            ) {
                if (chatMessages.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ESTABLECIENDO PROTOCOLO COGNITIVO...",
                            color = Color.White.copy(alpha = 0.35f),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "PREGUNTE: SENS, DISCIPLINA O MISIONES XL7",
                            color = Color.White.copy(alpha = 0.2f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(chatMessages) { msg ->
                            TerminalMsgItem(message = msg)
                        }

                        if (isAiLoading) {
                            item {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val infiniteTransition = rememberInfiniteTransition(label = "loader")
                                    val loaderAlpha by infiniteTransition.animateFloat(
                                        initialValue = 0.1f,
                                        targetValue = 1.0f,
                                        animationSpec = infiniteRepeatable(tween(400, easing = LinearEasing), RepeatMode.Reverse),
                                        label = "loaderA"
                                    )
                                    Text(
                                        text = "XL7_ORACLE IS ANALYZING SYSTEM ▉",
                                        color = Color.White.copy(alpha = loaderAlpha),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Command input row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    textStyle = TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.02f),
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribir comando...", color = Color.White.copy(alpha = 0.3f), fontSize = 13.sp, fontFamily = FontFamily.Monospace) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val prompt = textInput
                        textInput = ""
                        viewModel.sendAiMessage(prompt)
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    modifier = Modifier.height(56.dp).border(1.dp, Color.White, RoundedCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send Command")
                }
            }
        }
    }
}

@Composable
fun TerminalMsgItem(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bg = if (message.isUser) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.03f)
    val header = if (message.isUser) ">> COMANDO_MEMORIA" else "## ORÁCULO_XL7"
    val borderCol = if (message.isUser) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.12f)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Text(
            text = header,
            color = Color.White.copy(alpha = 0.45f),
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp).padding(bottom = 3.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, borderCol, RoundedCornerShape(4.dp))
                .background(bg)
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun BinaryTransmitterScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var rawText by remember { mutableStateOf("") }
    var binaryText by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "DISPOSITIVO: BINARY_COM",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Input raw text -> Binary Encoder
            Text(
                text = "ENTRADA DE TEXTO COMÚN:",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            TextField(
                value = rawText,
                onValueChange = {
                    rawText = it
                    // Convert to binary
                    binaryText = it.map { char ->
                        val b = Integer.toBinaryString(char.code)
                        b.padStart(8, '0')
                    }.joinToString(" ")
                },
                textStyle = TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.04f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.02f)
                ),
                modifier = Modifier.fillMaxWidth().height(100.dp),
                placeholder = { Text("Escriba su mensaje secreto aquí...", color = Color.White.copy(alpha = 0.3f), fontSize = 13.sp, fontFamily = FontFamily.Monospace) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Binary Output Decoder
            Text(
                text = "SALIDA EN BINARIO DESBORDANTE (01111000):",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.03f))
                    .padding(12.dp)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Text(
                            text = if (binaryText.isBlank()) "SISTEMA EN ESPERA..." else binaryText,
                            color = if (binaryText.isBlank()) Color.White.copy(alpha = 0.25f) else Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PomodoroFocusScreen(
    viewModel: TerminalViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val secLeft by viewModel.pomodoroSecondsLeft.collectAsState()
    val isRunning by viewModel.pomodoroIsRunning.collectAsState()

    val minutes = secLeft / 60
    val seconds = secLeft % 60
    val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "SECTOR: FOCUS_POM_SINC",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Big ticking timing dial
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                // Pulse background circle based on active cycle
                val infiniteTransition = rememberInfiniteTransition(label = "pomoDial")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 0.95f,
                    targetValue = 1.05f,
                    animationSpec = infiniteRepeatable(
                        tween(1000, easing = FastOutSlowInEasing),
                        RepeatMode.Reverse
                    ),
                    label = "pulseDial"
                )

                CircularProgressIndicator(
                    progress = { if (isRunning) secLeft / 1500f else 1f },
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    trackColor = Color.White.copy(alpha = 0.08f)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formattedTime,
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = if (isRunning) "ACTIVADO_" else "DETENIDO",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Control Buttons rows
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.togglePomodoro() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) Color.Transparent else Color.White,
                        contentColor = if (isRunning) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Close else Icons.Default.PlayArrow,
                        contentDescription = "Trigger Focus Clock"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRunning) "PAUSAR" else "START",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.resetPomodoro() },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset Timer", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "REINICIAR",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
fun CryptoKeyGeneratorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var generatedHexKey by remember { mutableStateOf("XL7-DECRYPT-CORE") }
    // Free Fire recommended sensi generator variables
    var ffSensiDpi by remember { mutableStateOf("940 DPI") }
    var ffSensiGral by remember { mutableStateOf("96%") }
    var ffSensiRedDot by remember { mutableStateOf("88%") }

    val cryptoChars = "0123456789ABCDEF"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "DISPOSITIVO: KEYGRID_GENERATOR",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Cryptographic key segment
            Text(
                text = "CONSTRUCTOR DE FIRMA CRIPTOGRÁFICA (AES_256):",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.04f))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = generatedHexKey,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = {
                        val build = StringBuilder("XL7-")
                        for (i in 0 until 4) {
                            build.append(cryptoChars.random())
                        }
                        build.append("-")
                        for (i in 0 until 4) {
                            build.append(cryptoChars.random())
                        }
                        build.append("-")
                        for (i in 0 until 4) {
                            build.append(cryptoChars.random())
                        }
                        generatedHexKey = build.toString()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(2.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp).border(1.dp, Color.White, RoundedCornerShape(2.dp))
                ) {
                    Text("GENERAR", fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sensi optimizer segment
            Text(
                text = "SENSIBILIDAD OPTIMIZADA AI (FREE FIRE):",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.02f))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SensiConfigField(label = "Sugerencia de DPI Físico:", value = ffSensiDpi)
                SensiConfigField(label = "Sensibilidad General FF:", value = ffSensiGral)
                SensiConfigField(label = "Mira de Punto Rojo:", value = ffSensiRedDot)

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        // Generate random strategic configurations
                        val dpis = listOf("480 DPI", "500 DPI", "800 DPI", "920 DPI", "1000 DPI")
                        val grals = listOf("94%", "97%", "99%", "100%", "92%")
                        val dots = listOf("85%", "90%", "87%", "82%", "94%")

                        ffSensiDpi = dpis.random()
                        ffSensiGral = grals.random()
                        ffSensiRedDot = dots.random()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth().height(44.dp).border(1.dp, Color.White, RoundedCornerShape(4.dp))
                ) {
                    Text("CALCULAR SENSIBILIDAD ENLACE", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SensiConfigField(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        Text(text = value, color = Color.White, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
    }
}
