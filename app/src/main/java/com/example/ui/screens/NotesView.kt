package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
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
import com.example.TerminalViewModel
import com.example.db.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotesView(
    viewModel: TerminalViewModel,
    modifier: Modifier = Modifier
) {
    val allNotes by viewModel.allNotes.collectAsState()
    var selectedTab by remember { mutableStateOf("ALL") } // "ALL", "IDEA", "META", "PROYECTO", "FRASE"
    var showAddDialog by remember { mutableStateOf(false) }
    var viewingNote by remember { mutableStateOf<Note?>(null) }

    val categories = listOf("ALL", "IDEA", "META", "PROYECTO", "FRASE")

    val filteredNotes = remember(allNotes, selectedTab) {
        if (selectedTab == "ALL") allNotes
        else allNotes.filter { it.noteType == selectedTab }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Screen Header details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SECTOR: NOTAS_Y_REGISTROS",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "[ CANT: ${allNotes.size} ]",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Categories horizontal tabs bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                categories.forEach { tab ->
                    val isActive = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isActive) Color.White else Color.White.copy(alpha = 0.05f))
                            .clickable { selectedTab = tab }
                            .border(1.dp, if (isActive) Color.White else Color.White.copy(alpha = 0.12f), RoundedCornerShape(2.dp))
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (tab == "ALL") "TODO" else tab,
                            color = if (isActive) Color.Black else Color.White.copy(alpha = 0.7f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Rapid Idea Saver Console Input
            var quickText by remember { mutableStateOf("") }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.03f))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "X7_IN >> ",
                    color = Color.White.copy(alpha = 0.6f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                BasicTextField(
                    value = quickText,
                    onValueChange = { quickText = it },
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = Color.White
                    ),
                    singleLine = true,
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    decorationBox = { innerTextField ->
                        if (quickText.isEmpty()) {
                            Text(
                                text = "Escribe una idea rápida...",
                                color = Color.White.copy(alpha = 0.25f),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        }
                        innerTextField()
                    }
                )

                // Submit Button
                IconButton(
                    onClick = {
                        if (quickText.isNotBlank()) {
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val dateStr = sdf.format(Date())
                            viewModel.createNote(
                                title = "Idea Rápida - $dateStr",
                                content = quickText.trim(),
                                type = "IDEA"
                            )
                            quickText = ""
                        }
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Quick Save",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Notes list
            if (filteredNotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.01f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "NO SE DETECTAN REGISTROS",
                            color = Color.White.copy(alpha = 0.3f),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "USE EL PROTOCOLO [+] PARA REGISTRAR",
                            color = Color.White.copy(alpha = 0.2f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredNotes) { note ->
                        NoteListItem(
                            note = note,
                            onClick = { viewingNote = note },
                            onPurge = { viewModel.deleteNote(note) }
                        )
                    }
                }
            }
        }

        // Floating cyber additions button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = Color.White,
            contentColor = Color.Black,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 12.dp)
                .border(1.dp, Color.White, RoundedCornerShape(4.dp))
        ) {
            Icon(Icons.Default.Add, contentDescription = "New Entry Action")
        }

        // Expanded Note Editor dialogue modal
        if (showAddDialog) {
            AddNoteConsole(
                onDismiss = { showAddDialog = false },
                onSave = { title, content, type ->
                    viewModel.createNote(title, content, type)
                    showAddDialog = false
                }
            )
        }

        // Expanded note viewer overlay modal
        if (viewingNote != null) {
            NoteDetailsOverlay(
                note = viewingNote!!,
                onDismiss = { viewingNote = null },
                onPurge = {
                    viewModel.deleteNote(viewingNote!!)
                    viewingNote = null
                }
            )
        }
    }
}

@Composable
fun NoteListItem(
    note: Note,
    onClick: () -> Unit,
    onPurge: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
    val timestampStr = sdf.format(Date(note.timestamp))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(4.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .clickable { onClick() }
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = note.noteType,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = timestampStr,
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1
            )
        }

        IconButton(onClick = onPurge) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Purge Note Entry",
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun AddNoteConsole(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("IDEA") } // "IDEA", "META", "PROYECTO", "FRASE"

    val infiniteTransition = rememberInfiniteTransition(label = "terminalCursor")
    val alphaCursor by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, easing = LinearEasing), RepeatMode.Reverse),
        label = "cursor"
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "XL7: NUEVO_REGISTRO_CRITICO",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Abrupt Exit",
                        tint = Color.White
                    )
                }
            }

            // Note Type Selection Grid
            Text(
                text = "DISPARADOR / CATEGORIA:",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val types = listOf("IDEA", "META", "PROYECTO", "FRASE")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                types.forEach { t ->
                    val selected = type == t
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (selected) Color.White else Color.Transparent)
                            .border(1.dp, if (selected) Color.White else Color.White.copy(alpha = 0.2f), RoundedCornerShape(2.dp))
                            .clickable { type = t }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = t,
                            color = if (selected) Color.Black else Color.White,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Title input
            Text(
                text = "TÍTULO:",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            TextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 14.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.04f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.02f),
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                placeholder = { Text("Asunto principal...", color = Color.White.copy(alpha = 0.25f), fontSize = 14.sp, fontFamily = FontFamily.Monospace) }
            )

            // Content body input
            Text(
                text = "REGISTRO / CONTENIDO:",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.03f))
                    .padding(12.dp)
            ) {
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    textStyle = TextStyle(color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 13.sp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxSize(),
                    placeholder = {
                        Row {
                            Text(
                                text = "Escribiendo cuerpo del bitácora",
                                color = Color.White.copy(alpha = 0.25f),
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "▉",
                                color = Color.White.copy(alpha = alphaCursor),
                                fontSize = 13.sp
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save actions
            Button(
                onClick = { if (title.isNotBlank() && content.isNotBlank()) onSave(title, content, type) },
                enabled = title.isNotBlank() && content.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.White.copy(alpha = 0.15f),
                    disabledContentColor = Color.White.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = "GUARDAR EN NÚCLEO",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun NoteDetailsOverlay(
    note: Note,
    onDismiss: () -> Unit,
    onPurge: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    val timestampStr = sdf.format(Date(note.timestamp))

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.94f)
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
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .background(Color.Black)
                    .padding(24.dp)
            ) {
                // Header console bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = note.noteType,
                            color = Color.Black,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close overlay viewer",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "S_TIMESTAMP: $timestampStr",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                Text(
                    text = note.title.uppercase(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(16.dp))

                // Content text scroll viewport
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                ) {
                    Text(
                        text = note.content,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(20.dp))

                // Action purge bottom row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onPurge,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Purge note entry", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PURGAR DEFINITIVO",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}
