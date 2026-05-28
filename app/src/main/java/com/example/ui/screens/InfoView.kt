package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.InfoCategory
import com.example.PersonProfile
import com.example.TerminalViewModel
import kotlinx.coroutines.delay

@Composable
fun InfoView(
    viewModel: TerminalViewModel,
    modifier: Modifier = Modifier
) {
    val activeCategory by viewModel.activeInfoCategory.collectAsState()
    val focusedPerson by viewModel.focusedPerson.collectAsState()

    var activeDetailItem by remember { mutableStateOf<GenericRecord?>(null) }

    // Pre-populate private profiles
    val peopleProfiles = remember {
        listOf(
            PersonProfile(
                name = "Gamersito",
                avatarType = 0,
                role = "Fundador de XL7",
                status = "Activo",
                goals = "Construir una comunidad grande, estructurada y ayudar personas.",
                notes = "Mejorando disciplina, consistencia y proyectos de red.",
                relation = "Líder de Operaciones XL7"
            ),
            PersonProfile(
                name = "Nexus-9",
                avatarType = 1,
                role = "AI Architect & Dev Core",
                status = "Estable",
                goals = "Automatizar herramientas de hacking y apoyo de productividad.",
                notes = "Alineado con el protocolo de la terminal directiva.",
                relation = "IA Butler Central del sistema"
            ),
            PersonProfile(
                name = "CyberClips",
                avatarType = 2,
                role = "Gaming Lead & Editor",
                status = "Monitoreando",
                goals = "Elevar el nivel competitivo de la comunidad Free Fire.",
                notes = "Encargado de archivar clips y coordinar creadores de la facción.",
                relation = "Coordinador Multimedia"
            )
        )
    }

    // Projects, Ideas, Community, Archives Generic lists
    val projectsList = remember {
        listOf(
            GenericRecord("PROY_RED_01", "CASAS XL7 CENTRAL HUB", "Desarrollo de canales secretos y bases de datos para organización de torneos privados y control directivo general.", "Completado al 85%"),
            GenericRecord("PROY_AI_SENS", "TACTILE ALGORITHM MODEL", "Mecanismo inteligente con respuestas de sensibilidad táctil móvil, adaptando puntería táctica para la comunidad Free Fire.", "Completado / Activo"),
            GenericRecord("PROY_GG_ARENA", "MULTIPLAYER ESUD PROT", "Planificación de salas de guerra competitivas y clips enlazados para reclutamiento directo de asaltantes.", "En Planificación")
        )
    }

    val ideasList = remember {
        listOf(
            GenericRecord("IDEA_LOCK_01", "DEAD MAN SYSTEM OVERRIDE", "Protocolo de auto-bloqueo total. Si la app se minimiza o se sacude el terminal, activa inmediatamente el reloj digital común.", "Propuesto"),
            GenericRecord("IDEA_SCRIM_02", "RECOMPENSAS DESCENTRALIZADAS", "Incentivos directos de reputación a creadores de clips competitivos que lideran salas de Free fire.", "Aprobado")
        )
    }

    val communityList = remember {
        listOf(
            GenericRecord("EST_SERVER", "ESTADO DE CONSULAS XL7", "RED INTEGRADA: 12 servidores redundantes, encriptación local extrema en dispositivo active.", "PING: 14ms (Estable)"),
            GenericRecord("EST_RECRUIT", "PLAN DE ESCUADRA GAMER", "Reclutamiento directo abierto. El staff supervisa el envío de fichas técnicas para escuadra XL7 Esports.", "Activo")
        )
    }

    val archivesList = remember {
        listOf(
            GenericRecord("ARCH_001_ENC", "X7 KERNEL TOKEN STRING", "Criptograma de autenticidad de la central: 784c375f4b65726e656c5f41637469766f", "Encriptado hex / Seguro"),
            GenericRecord("ARCH_002_GPS", "XL7 REDUNDANCE NETWORK", "Protocolo alternativo de transferencia de paquetes seguro SSL para sincronizar notas fuera del área local.", "Cifrado Militar")
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Section Header
            Text(
                text = "SECTOR: BASE_DE_DATOS_XL7",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // Categories horizontal grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val subcats = listOf(
                    InfoCategory.ALL to "TODO",
                    InfoCategory.PERSONAS to "PERSONAS",
                    InfoCategory.PROYECTOS to "PROYECTOS",
                    InfoCategory.IDEAS to "IDEAS",
                    InfoCategory.COMUNIDAD to "COMUNIDAD",
                    InfoCategory.ARCHIVOS to "ARCHIVOS"
                )

                subcats.forEach { (cat, label) ->
                    val isActive = activeCategory == cat
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isActive) Color.White else Color.White.copy(alpha = 0.05f))
                            .clickable { viewModel.selectInfoCategory(cat) }
                            .border(1.dp, if (isActive) Color.White else Color.White.copy(alpha = 0.12f), RoundedCornerShape(2.dp))
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (isActive) Color.Black else Color.White,
                            fontSize = 7.5.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }

            // Results List
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Determine sections to show
                val showPeople = activeCategory == InfoCategory.ALL || activeCategory == InfoCategory.PERSONAS
                val showProjects = activeCategory == InfoCategory.ALL || activeCategory == InfoCategory.PROYECTOS
                val showIdeas = activeCategory == InfoCategory.ALL || activeCategory == InfoCategory.IDEAS
                val showCommunity = activeCategory == InfoCategory.ALL || activeCategory == InfoCategory.COMUNIDAD
                val showArchives = activeCategory == InfoCategory.ALL || activeCategory == InfoCategory.ARCHIVOS

                if (showPeople) {
                    item { SectionSpacerLabel("MI PERFIL CORPORATIVO") }
                    item {
                        MyProfileCard(viewModel = viewModel)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item { SectionSpacerLabel("REGISTRO DE EXPEDIENTES (PERSONAS)") }
                    items(peopleProfiles) { person ->
                        ProfileListItem(person = person, onClick = { viewModel.focusPerson(person) })
                    }
                }

                if (showProjects) {
                    item { SectionSpacerLabel("RED DE PROYECTOS TÁCTICOS (PROYECTOS)") }
                    items(projectsList) { rec ->
                        GenericRecordItem(record = rec, onClick = { activeDetailItem = rec })
                    }
                }

                if (showIdeas) {
                    item { SectionSpacerLabel("SISTEMA DE IDEAS DE RESILENCIA (IDEAS)") }
                    items(ideasList) { rec ->
                        GenericRecordItem(record = rec, onClick = { activeDetailItem = rec })
                    }
                }

                if (showCommunity) {
                    item { SectionSpacerLabel("PANEL DE ESTADOS COMUNITARIOS (COMUNIDAD)") }
                    items(communityList) { rec ->
                        GenericRecordItem(record = rec, onClick = { activeDetailItem = rec })
                    }
                }

                if (showArchives) {
                    item { SectionSpacerLabel("CÚMULO DE ARCHIVOS ENCRIPTADOS (ARCHIVOS)") }
                    items(archivesList) { rec ->
                        GenericRecordItem(record = rec, onClick = { activeDetailItem = rec })
                    }
                }
            }
        }

        // Focused Person details overlay modal
        if (focusedPerson != null) {
            PersonFileOverlay(
                person = focusedPerson!!,
                onDismiss = { viewModel.focusPerson(null) }
            )
        }

        // Generic Records details overlay modal (glitched text briefs)
        if (activeDetailItem != null) {
            RecordDecoderOverlay(
                record = activeDetailItem!!,
                onDismiss = { activeDetailItem = null }
            )
        }
    }
}

@Composable
fun SectionSpacerLabel(label: String) {
    Text(
        text = ":: $label",
        color = Color.White.copy(alpha = 0.45f),
        fontFamily = FontFamily.Monospace,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
    )
}

@Composable
fun ProfileListItem(
    person: PersonProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
            .background(Color.White.copy(alpha = 0.02f))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // High-Tech Cyber Avatar (Canvas mini)
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerVal = Offset(size.width / 2f, size.height / 2f)
                val radiusVal = size.width / 2.3f
                drawCircle(Color.White.copy(alpha = 0.1f), center = centerVal, radius = radiusVal)

                when (person.avatarType) {
                    0 -> { // Gamersito specific high weight geometric profile shape
                        drawCircle(Color.White, center = centerVal, radius = radiusVal / 2f, style = Stroke(width = 2.dp.toPx()))
                        drawCircle(Color.White, center = centerVal, radius = radiusVal / 4f)
                    }
                    1 -> { // Tech hexagon blueprint
                        drawLine(Color.White, start = Offset(size.width * 0.2f, size.height * 0.5f), end = Offset(size.width * 0.5f, size.height * 0.2f), strokeWidth = 2f)
                        drawLine(Color.White, start = Offset(size.width * 0.5f, size.height * 0.2f), end = Offset(size.width * 0.8f, size.height * 0.5f), strokeWidth = 2f)
                        drawLine(Color.White, start = Offset(size.width * 0.8f, size.height * 0.5f), end = Offset(size.width * 0.5f, size.height * 0.8f), strokeWidth = 2f)
                        drawLine(Color.White, start = Offset(size.width * 0.5f, size.height * 0.8f), end = Offset(size.width * 0.2f, size.height * 0.5f), strokeWidth = 2f)
                    }
                    else -> { // Technical lines
                        drawLine(Color.White, start = Offset(0f, size.height / 2f), end = Offset(size.width, size.height / 2f), strokeWidth = 1.5f)
                        drawLine(Color.White, start = Offset(size.width / 2f, 0f), end = Offset(size.width / 2f, size.height), strokeWidth = 1.5f)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = person.name.uppercase(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = person.role,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Scanning Pulse Indicator Status
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "pulseStatus")
            val alphaPulse by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(700, easing = LinearEasing), RepeatMode.Reverse),
                label = "pulse"
            )

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = alphaPulse))
            )
            Text(
                text = person.status.uppercase(),
                color = Color.White,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun GenericRecordItem(
    record: GenericRecord,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
            .background(Color.White.copy(alpha = 0.01f))
            .clickable { onClick() }
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = record.code,
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = record.title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
        Box(
            modifier = Modifier
                .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "INFO_READ",
                color = Color.White,
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun PersonFileOverlay(
    person: PersonProfile,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scannerY by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            for (i in 0..100) {
                scannerY = i / 100f
                delay(20)
            }
            for (i in 100 downTo 0) {
                scannerY = i / 100f
                delay(20)
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.96f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                    .background(Color.Black)
                    .padding(24.dp)
            ) {
                // Header Close Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "XL7SEC_DOSSIER: " + person.name.uppercase(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close File", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Diagnostic Big Avatar Canvas with scanner bar
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .align(Alignment.CenterHorizontally)
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .background(Color.White.copy(alpha = 0.02f)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeVal = 1.dp.toPx()
                        // Draw concentric diagnostic radar circles
                        drawCircle(Color.White.copy(alpha = 0.1f), radius = size.width / 2.5f, style = Stroke(width = strokeVal))
                        drawCircle(Color.White.copy(alpha = 0.05f), radius = size.width / 4f, style = Stroke(width = strokeVal))

                        // Draw abstract silhouette face representation
                        val centerOffset = Offset(size.width / 2f, size.height / 2f)
                        drawCircle(Color.White, center = Offset(centerOffset.x, centerOffset.y - 12f), radius = 22f, style = Stroke(width = 2.dp.toPx()))
                        drawCircle(Color.White, center = Offset(centerOffset.x, centerOffset.y + 32f), radius = 38f, style = Stroke(width = 2.dp.toPx()))

                        // Crosshairs details
                        drawLine(Color.White.copy(alpha = 0.2f), start = Offset(0f, size.height / 2f), end = Offset(size.width, size.height / 2f))
                        drawLine(Color.White.copy(alpha = 0.2f), start = Offset(size.width / 2f, 0f), end = Offset(size.width / 2f, size.height))

                        // Dynamic scanline sweep
                        val lineY = size.height * scannerY
                        drawLine(Color.White.copy(alpha = 0.7f), start = Offset(0f, lineY), end = Offset(size.width, lineY), strokeWidth = 3f)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Person Meta stats details list
                val stats = listOf(
                    "Nombre de Agente:" to person.name,
                    "Rol Técnico:" to person.role,
                    "Estatus Operacional:" to person.status,
                    "Objetivos Centrales:" to person.goals,
                    "Bitácora / Notas:" to person.notes,
                    "Relación de Red:" to person.relation
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(stats) { (key, valStr) ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = key.uppercase(),
                                color = Color.White.copy(alpha = 0.45f),
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = valStr,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "LA CONSOLA XL7 DECLARA LA INTEGRIDAD TOTAL DE ESTA INVESTIGACIÓON. CLAVE_777_OK.",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 8.5.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun RecordDecoderOverlay(
    record: GenericRecord,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.94f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .background(Color.Black)
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DESBLOQUEANDO: " + record.code,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Exit decoder", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Decrypt lines effect
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.02f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "LLAVE: SHA-256 / DIRECT_GATEWAY ... COMPLETO",
                        color = Color.White.copy(alpha = 0.5f),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "TÍTULO DE CONTROL:",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = record.title.uppercase(),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                )

                Text(
                    text = "DESCRIPCIÓN DE EXPEDIENTE:",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = record.desc,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ESTADO DE EJECUCIÓN: " + record.status.uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

data class GenericRecord(
    val code: String,
    val title: String,
    val desc: String,
    val status: String
)

@Composable
fun MyProfileCard(
    viewModel: TerminalViewModel,
    modifier: Modifier = Modifier
) {
    val name by viewModel.profileName.collectAsState()
    val quote by viewModel.profileQuote.collectAsState()
    val status by viewModel.profileStatus.collectAsState()
    val level by viewModel.profileLevel.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(4.dp))
            .clickable { showEditDialog = true }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // High-tech Animated Biomarker Scanner (Representing profile photo)
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "scanner")
                val scanOffset by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1800, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scanLine"
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Draw military crosshair
                    drawLine(
                        color = Color.White.copy(alpha = 0.15f),
                        start = Offset(0f, size.height/2),
                        end = Offset(size.width, size.height/2),
                        strokeWidth = 1f
                    )
                    drawLine(
                        color = Color.White.copy(alpha = 0.15f),
                        start = Offset(size.width/2, 0f),
                        end = Offset(size.width/2, size.height),
                        strokeWidth = 1f
                    )
                    // Scanner line sweeps
                    drawLine(
                        color = Color.White.copy(alpha = 0.6f),
                        start = Offset(0f, scanOffset * size.height),
                        end = Offset(size.width, scanOffset * size.height),
                        strokeWidth = 2.5f
                    )
                    // Rotating target rings
                    drawCircle(
                        color = Color.White.copy(alpha = 0.2f),
                        radius = size.width * 0.35f,
                        style = Stroke(width = 1.0f)
                    )
                }

                Text(
                    text = "X7",
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MI PERFIL FUNDADOR",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "LV.$level",
                        color = Color.White,
                        fontSize = 9.5.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "“$quote”",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Real-time select state indicator matching requirement 9 ("Estado XL7")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = status.uppercase(),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 8.5.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showEditDialog) {
        ProfileEditDialog(
            viewModel = viewModel,
            onDismiss = { showEditDialog = false }
        )
    }
}

@Composable
fun ProfileEditDialog(
    viewModel: TerminalViewModel,
    onDismiss: () -> Unit
) {
    val currentName by viewModel.profileName.collectAsState()
    val currentQuote by viewModel.profileQuote.collectAsState()
    val currentStatus by viewModel.profileStatus.collectAsState()
    val currentLevel by viewModel.profileLevel.collectAsState()

    var nameInput by remember { mutableStateOf(currentName) }
    var quoteInput by remember { mutableStateOf(currentQuote) }
    var levelInput by remember { mutableStateOf(currentLevel) }
    var selectedStatus by remember { mutableStateOf(currentStatus) }

    val presetStatuses = listOf(
        "Modo disciplina.",
        "Sistema estable.",
        "Objetivo en progreso.",
        "XL7 conectado."
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.95f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.2.dp, Color.White, RoundedCornerShape(4.dp))
                    .background(Color.Black)
                    .padding(20.dp)
            ) {
                Text(
                    text = "RECONFIGURAR PERFIL XL7",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Editable Fields
                Text("NOMBRE FUNDADOR", color = Color.White.copy(alpha = 0.5f), fontSize = 8.5.sp, fontFamily = FontFamily.Monospace)
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    textStyle = TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                Text("FRASE PERSONAL", color = Color.White.copy(alpha = 0.5f), fontSize = 8.5.sp, fontFamily = FontFamily.Monospace)
                OutlinedTextField(
                    value = quoteInput,
                    onValueChange = { quoteInput = it },
                    textStyle = TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                Text("NIVEL REGISTRADO", color = Color.White.copy(alpha = 0.5f), fontSize = 8.5.sp, fontFamily = FontFamily.Monospace)
                OutlinedTextField(
                    value = levelInput,
                    onValueChange = { levelInput = it },
                    textStyle = TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                Text("PRESET ESTADO XL7", color = Color.White.copy(alpha = 0.5f), fontSize = 8.5.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(bottom = 6.dp))
                presetStatuses.forEach { st ->
                    val isSelected = selectedStatus == st
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .border(1.dp, if (isSelected) Color.White else Color.White.copy(alpha = 0.15f), RoundedCornerShape(2.dp))
                            .background(if (isSelected) Color.White.copy(alpha = 0.08f) else Color.Transparent)
                            .clickable { selectedStatus = st }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.25f))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = st,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                            fontSize = 10.5.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.White),
                        shape = RoundedCornerShape(2.dp),
                        modifier = Modifier.weight(1f).border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
                    ) {
                        Text("CANCELAR", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            viewModel.updateProfile(
                                name = nameInput.trim(),
                                quote = quoteInput.trim(),
                                status = selectedStatus,
                                level = levelInput.trim()
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                        shape = RoundedCornerShape(2.dp),
                        modifier = Modifier.weight(1f).border(1.dp, Color.White, RoundedCornerShape(2.dp))
                    ) {
                        Text("GUARDAR RECON", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
