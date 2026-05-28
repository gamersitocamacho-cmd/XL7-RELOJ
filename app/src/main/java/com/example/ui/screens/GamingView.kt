package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.EsportsLog
import com.example.GamingSystem
import com.example.TerminalViewModel
import kotlinx.coroutines.delay

@Composable
fun GamingView(
    viewModel: TerminalViewModel,
    modifier: Modifier = Modifier
) {
    val activeSys by viewModel.activeGamingSystem.collectAsState()

    AnimatedContent(
        targetState = activeSys,
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                    slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        },
        label = "GamingTransition"
    ) { sys ->
        when (sys) {
            GamingSystem.MENU -> GamingSelectionLobby(onSelect = { viewModel.selectGaming(it) }, modifier = modifier)
            GamingSystem.STATS -> FreeFireStatsScreen(viewModel = viewModel, onBack = { viewModel.selectGaming(GamingSystem.MENU) }, modifier = modifier)
            GamingSystem.CLIPS -> ClipsArchiveScreen(onBack = { viewModel.selectGaming(GamingSystem.MENU) }, modifier = modifier)
            GamingSystem.LOGS -> SquadCommunityScreen(viewModel = viewModel, onBack = { viewModel.selectGaming(GamingSystem.MENU) }, modifier = modifier)
        }
    }
}

@Composable
fun GamingSelectionLobby(
    onSelect: (GamingSystem) -> Unit,
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
                text = "SECTOR: ARENA_GAMING_LOBBY",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GamerLobbyCard(
                    title = "TARJETA COMPETITIVA FF",
                    subtitle = "Gamer Tracker",
                    desc = "Registro y personalización de tu perfil e ID táctico de Free Fire. Visualiza rangos, headshot rates de combate y nivel directivo.",
                    symbol = "FF_ID",
                    onClick = { onSelect(GamingSystem.STATS) },
                    modifier = Modifier.weight(1f)
                )
                GamerLobbyCard(
                    title = "CLIPS DESTACADOS XL7",
                    subtitle = "Archivo Multimedia",
                    desc = "Visualiza el registro grabado de jugadas magistrales. Reproducción desaturada simulada con telemetría de combate y barridos de radar.",
                    symbol = "REC_CLIP",
                    onClick = { onSelect(GamingSystem.CLIPS) },
                    modifier = Modifier.weight(1f)
                )
                GamerLobbyCard(
                    title = "SQUAD CANAL DE VOZ LOGS",
                    subtitle = "Comunidad de Combate",
                    desc = "Chat directo de la directiva secreta y reportes competitivos. Sincroniza tácticas de escuadra e ingresos con otros agentes.",
                    symbol = "COMM_CH",
                    onClick = { onSelect(GamingSystem.LOGS) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun GamerLobbyCard(
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
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.02f))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = symbol,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = subtitle.uppercase(),
                    color = Color.White.copy(alpha = 0.45f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(top = 1.dp, bottom = 4.dp)
                )
                Text(
                    text = desc,
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 10.5.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun FreeFireStatsScreen(
    viewModel: TerminalViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nick by viewModel.gNick.collectAsState()
    val id by viewModel.gID.collectAsState()
    val lv by viewModel.gLevel.collectAsState()
    val rank by viewModel.gRank.collectAsState()
    val hs by viewModel.gHsRate.collectAsState()
    val wr by viewModel.gWinRate.collectAsState()

    var isEditing by remember { mutableStateOf(false) }

    // Backup states for edit fields
    var editNick by remember { mutableStateOf(nick) }
    var editID by remember { mutableStateOf(id) }
    var editLv by remember { mutableStateOf(lv) }
    var editRank by remember { mutableStateOf(rank) }
    var editHs by remember { mutableStateOf(hs) }
    var editWr by remember { mutableStateOf(wr) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "TARJETA: CONSOLA_FREE_FIRE",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "X7_SENS_TRUE",
                    color = Color.White.copy(alpha = 0.4f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
            }

            if (!isEditing) {
                // Large Holographic HUD Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.02f))
                        .padding(20.dp)
                ) {
                    Column {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "FICHA_MILITAR:: FREE_FIRE",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Box(
                                modifier = Modifier
                                    .border(1.dp, Color.White, RoundedCornerShape(2.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "RANK: $rank",
                                    color = Color.Black,
                                    fontSize = 8.5.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Big Nickname
                        Text(
                            text = nick.uppercase(),
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "COMPETITIVE_ID: $id | LV: $lv",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                        Spacer(modifier = Modifier.height(20.dp))

                        // Stats grids
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "HEADSHOT RATE:",
                                    color = Color.White.copy(alpha = 0.45f),
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = hs,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "WIN RATE GLOBAL:",
                                    color = Color.White.copy(alpha = 0.45f),
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = wr,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Holographic grid design canvas component
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val drawCol = Color.White.copy(alpha = 0.08f)
                                // Draw high-tech horizontal sweep waves
                                for (i in 0..10) {
                                    val yOffset = size.height * (i / 10f)
                                    drawLine(drawCol, start = Offset(0f, yOffset), end = Offset(size.width, yOffset))
                                }
                                // Center targets
                                drawCircle(Color.White.copy(alpha = 0.15f), center = Offset(size.width / 2f, size.height / 2f), radius = 30f, style = Stroke(width = 1f))
                                drawCircle(Color.White.copy(alpha = 0.07f), center = Offset(size.width / 2f, size.height / 2f), radius = 60f, style = Stroke(width = 1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action to edit card
                Button(
                    onClick = {
                        editNick = nick
                        editID = id
                        editLv = lv
                        editRank = rank
                        editHs = hs
                        editWr = wr
                        isEditing = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Card stats")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EDITAR EXPEDIENTE",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            } else {
                // Editing Layout Screen
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "MODIFICANDO EXPEDIENTE MILITAR:",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        EditFieldComponent(label = "APODO DE COMBATE:", value = editNick, onValueChange = { editNick = it })
                    }
                    item {
                        EditFieldComponent(label = "IDENTIFICACIÓN ID FF:", value = editID, onValueChange = { editID = it })
                    }
                    item {
                        EditFieldComponent(label = "NIVEL DIRECTIVO:", value = editLv, onValueChange = { editLv = it })
                    }
                    item {
                        EditFieldComponent(label = "RANGO DE COMBATE:", value = editRank, onValueChange = { editRank = it })
                    }
                    item {
                        EditFieldComponent(label = "HEADSHOT RATE (%):", value = editHs, onValueChange = { editHs = it })
                    }
                    item {
                        EditFieldComponent(label = "WIN RATE GLOBAL (%):", value = editWr, onValueChange = { editWr = it })
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.saveFreeFireStats(editNick, editID, editLv, editRank, editHs, editWr)
                                    isEditing = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.weight(1f).height(46.dp).border(1.dp, Color.White, RoundedCornerShape(4.dp))
                            ) {
                                Text("GUARDAR", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { isEditing = false },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.weight(1f).height(46.dp).border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                            ) {
                                Text("DESCARTAR", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditFieldComponent(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.02f),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Simulated Media Clips Archives Screen implementation
@Composable
fun ClipsArchiveScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedClipName by remember { mutableStateOf<String?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var playBackSpeed by remember { mutableStateOf(1.0f) }

    val clips = remember {
        listOf(
            "Gamersito - Quadra M1014 Scrim",
            "Aegis - Sensi Config 360",
            "Nexus-9 - Solo vs Squad Mill",
            "CyberClips - 1v4 Rush Hour"
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "REPROD: ARCH_DE_CLIPS",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (selectedClipName == null) {
                // List of clips
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(clips) { clip ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                                .background(Color.White.copy(alpha = 0.02f))
                                .clickable {
                                    selectedClipName = clip
                                    isPlaying = true
                                }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White.copy(alpha = 0.7f))
                                Text(
                                    text = clip.uppercase(),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "MONOCH_AVI",
                                color = Color.White.copy(alpha = 0.35f),
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            } else {
                // High-Tech Cyber desaturated player simulator!
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Video Canvas Screen Simulation
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(1.dp, Color.White, RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.04f)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Drawing scanning grid crosshairs on desaturated background
                        var scanProgress by remember { mutableStateOf(0f) }
                        LaunchedEffect(isPlaying) {
                            if (isPlaying) {
                                while (true) {
                                    scanProgress = (scanProgress + 0.01f) % 1.0f
                                    delay(16)
                                }
                            }
                        }

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Draw horizontal scanline matching play progress
                            val scanlineY = size.height * scanProgress
                            drawLine(Color.White.copy(alpha = 0.35f), start = Offset(0f, scanlineY), end = Offset(size.width, scanlineY), strokeWidth = 1.5f)

                            // Crosshairs
                            val cx = size.width / 2f
                            val cy = size.height / 2f
                            drawLine(Color.White.copy(alpha = 0.2f), start = Offset(cx - 40f, cy), end = Offset(cx + 40f, cy))
                            drawLine(Color.White.copy(alpha = 0.2f), start = Offset(cx, cy - 40f), end = Offset(cx, cy + 40f))
                            drawCircle(Color.White.copy(alpha = 0.08f), center = Offset(cx, cy), radius = 50f, style = Stroke(width = 1f))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isPlaying) "▶ ACTIVE_PLAY_H76" else "■ RECORD_PAUSED_X0",
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = if (isPlaying) "REPRODUCIENDO CLASP_" else "EN PAUSA",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = selectedClipName!!.uppercase(),
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated HUD controls
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .clickable { isPlaying = !isPlaying }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = if (isPlaying) "PAUSA" else "REPRODUCIR",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(onClick = { selectedClipName = null }) {
                                Icon(Icons.Default.Close, contentDescription = "Halt", tint = Color.White)
                            }
                        }

                        // Playback Speed modifier
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val speeds = listOf(1.0f, 1.5f, 2.0f)
                            speeds.forEach { spd ->
                                val active = playBackSpeed == spd
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(if (active) Color.White else Color.Transparent)
                                        .border(1.dp, if (active) Color.White else Color.White.copy(alpha = 0.2f), RoundedCornerShape(2.dp))
                                        .clickable { playBackSpeed = spd }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${spd}x",
                                        color = if (active) Color.Black else Color.White,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SquadCommunityScreen(
    viewModel: TerminalViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logs by viewModel.simulatedEsportsFeed.collectAsState()
    var rawLogsInput by remember { mutableStateOf("") }

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
                    text = "LOBBY: SQUAD_COMM_CANAL",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Messages scroll stream view
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.02f))
                    .padding(12.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(logs) { log ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "@${log.sender.uppercase()}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = log.time,
                                    color = Color.White.copy(alpha = 0.4f),
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = log.text,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 17.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Squad direct input chat action
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = rawLogsInput,
                    onValueChange = { rawLogsInput = it },
                    textStyle = TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.02f),
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Reportar reporte tactico...", color = Color.White.copy(alpha = 0.3f), fontSize = 12.sp, fontFamily = FontFamily.Monospace) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (rawLogsInput.isNotBlank()) {
                            val msg = rawLogsInput
                            rawLogsInput = ""
                            viewModel.postEsportsLog(msg)
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    modifier = Modifier.height(56.dp).border(1.dp, Color.White, RoundedCornerShape(4.dp))
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Post Log")
                }
            }
        }
    }
}
