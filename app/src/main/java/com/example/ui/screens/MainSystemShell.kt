package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.SubScreen
import com.example.TerminalViewModel

@Composable
fun MainSystemShell(
    viewModel: TerminalViewModel,
    modifier: Modifier = Modifier
) {
    val activeSubScreen by viewModel.activeSubScreen.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Black,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.02f))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Xl7AnimatedLogo()

                    // Display active profile status representing: "Estado XL7"
                    val currentStatus by viewModel.profileStatus.collectAsState()
                    Text(
                        text = "ESTADO: " + currentStatus.uppercase(),
                        color = Color.White.copy(alpha = 0.55f),
                        fontSize = 8.5.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Miniature tech clock on the right side
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        val timeStr by viewModel.timeString.collectAsState()
                        val dateStr by viewModel.dateString.collectAsState()
                        Text(
                            text = timeStr,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "GMT // " + dateStr.substring(0, kotlin.math.min(dateStr.length, 6)),
                            color = Color.White.copy(alpha = 0.45f),
                            fontSize = 7.5.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Close Terminal back to normal clock facade
                    Button(
                        onClick = { viewModel.lockSystem() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.White),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .height(28.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(4.dp)),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "Lock Gate", modifier = Modifier.size(11.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "CERRAR", fontSize = 8.5.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(6.dp))
                    .background(Color.Black)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val navItems = listOf(
                    NavigationDescriptor(SubScreen.NOTAS, "Notas", Icons.Default.Add),
                    NavigationDescriptor(SubScreen.INFO, "Info", Icons.Default.Info),
                    NavigationDescriptor(SubScreen.HERRAMIENTAS, "Lab", Icons.Default.Refresh),
                    NavigationDescriptor(SubScreen.GAMING, "Arena", Icons.Default.Lock)
                )

                navItems.forEach { item ->
                    val selected = activeSubScreen == item.screen
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (selected) Color.White else Color.Transparent)
                            .clickable { viewModel.navigateToSubScreen(item.screen) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (selected) Color.Black else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = item.label.uppercase(),
                                color = if (selected) Color.Black else Color.White.copy(alpha = 0.6f),
                                fontSize = 8.5.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Floating background particles
            ParticleBackground()

            Column(modifier = Modifier.fillMaxSize()) {
                val activeNotif by viewModel.activeNotification.collectAsState()

                AnimatedVisibility(
                    visible = activeNotif != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    activeNotif?.let { msg ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "SISTEMA INFO: $msg",
                                color = Color.White,
                                fontSize = 9.5.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = activeSubScreen,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(150)) togetherWith fadeOut(animationSpec = tween(150))
                        },
                        label = "ShellScreenTransition"
                    ) { sub ->
                        when (sub) {
                            SubScreen.NOTAS -> NotesView(viewModel = viewModel)
                            SubScreen.INFO -> InfoView(viewModel = viewModel)
                            SubScreen.HERRAMIENTAS -> ToolsView(viewModel = viewModel)
                            SubScreen.GAMING -> GamingView(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

data class NavigationDescriptor(
    val screen: SubScreen,
    val label: String,
    val icon: ImageVector
)

@Composable
fun Xl7AnimatedLogo(modifier: Modifier = Modifier) {
    var isStarted by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isStarted = true
    }

    val scaleVal by animateFloatAsState(
        targetValue = if (isStarted) 1f else 0.85f,
        animationSpec = tween(850, easing = EaseOutBack),
        label = "logoScale"
    )
    val alphaVal by animateFloatAsState(
        targetValue = if (isStarted) 1f else 0f,
        animationSpec = tween(900),
        label = "logoAlpha"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.graphicsLayer {
            scaleX = scaleVal
            scaleY = scaleVal
            alpha = alphaVal
        }
    ) {
        // High-tech logo bracket indicator (glitchy box)
        Box(
            modifier = Modifier
                .size(16.dp)
                .border(1.dp, Color.White, RoundedCornerShape(2.dp))
                .background(Color.White.copy(alpha = 0.15f * alphaVal)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color.White.copy(alpha = alphaVal))
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "TERMINAL XL7",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 2.sp
        )
    }
}
