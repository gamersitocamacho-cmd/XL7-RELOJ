package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.Content
import com.example.api.GeminiClient
import com.example.api.Part
import com.example.db.AppDatabase
import com.example.db.Note
import com.example.db.NoteRepository
import com.example.sound.SoundSynthesizer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

sealed class Screen {
    object Clock : Screen()
    object Unlocking : Screen()
    object MainTerminal : Screen()
}

enum class SubScreen {
    NOTAS, INFO, HERRAMIENTAS, GAMING
}

enum class InfoCategory {
    ALL, PERSONAS, PROYECTOS, IDEAS, COMUNIDAD, ARCHIVOS
}

enum class ToolSystem {
    MENU, AI_TERMINAL, BINARY_CODEC, POMODORO, KEYGRID
}

enum class GamingSystem {
    MENU, STATS, CLIPS, LOGS
}

class TerminalViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepository: NoteRepository
    val allNotes: StateFlow<List<Note>>

    init {
        val database = AppDatabase.getDatabase(application)
        noteRepository = NoteRepository(database.noteDao())
        allNotes = noteRepository.allNotes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // Navigation & System Status
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Clock)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _activeSubScreen = MutableStateFlow(SubScreen.NOTAS)
    val activeSubScreen: StateFlow<SubScreen> = _activeSubScreen.asStateFlow()

    // --- CARACTERÍSTICAS FUTURISTAS GENERALES ---
    // 1. Frases automáticas
    private val welcomePhrases = listOf(
        "Sistema XL7 activo.",
        "Construyendo algo grande.",
        "La confianza se demuestra.",
        "Modo enfoque activado.",
        "Bienvenido fundador."
    )
    private val _greetingPhrase = MutableStateFlow(welcomePhrases.random())
    val greetingPhrase: StateFlow<String> = _greetingPhrase.asStateFlow()

    fun rotateGreetingPhrase() {
        _greetingPhrase.value = welcomePhrases.random()
    }

    // 3. Notificación futurista con sonido
    private val _activeNotification = MutableStateFlow<String?>(null)
    val activeNotification: StateFlow<String?> = _activeNotification.asStateFlow()
    private var notificationJob: Job? = null

    fun postNotification(message: String) {
        notificationJob?.cancel()
        _activeNotification.value = message
        notificationJob = viewModelScope.launch {
            try {
                SoundSynthesizer.playConfirm()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(3000)
            _activeNotification.value = null
        }
    }

    // 7. Perfil XL7 (Enlazado al resto de la terminal)
    private val _profileName = MutableStateFlow("Gamersito")
    val profileName: StateFlow<String> = _profileName.asStateFlow()

    private val _profileQuote = MutableStateFlow("La confianza se demuestra.")
    val profileQuote: StateFlow<String> = _profileQuote.asStateFlow()

    private val _profileStatus = MutableStateFlow("Modo disciplina.")
    val profileStatus: StateFlow<String> = _profileStatus.asStateFlow()

    private val _profileLevel = MutableStateFlow("72")
    val profileLevel: StateFlow<String> = _profileLevel.asStateFlow()

    fun updateProfile(name: String, quote: String, status: String, level: String) {
        viewModelScope.launch {
            _profileName.value = name
            _profileQuote.value = quote
            _profileStatus.value = status
            _profileLevel.value = level

            // Sincronizar con el rastreador de juego FF
            _gNick.value = name
            _gLevel.value = level

            postNotification("PERFIL XL7 ACTUALIZADO.")
        }
    }

    // Clock Facade State
    private val _timeString = MutableStateFlow("12:45:00")
    val timeString: StateFlow<String> = _timeString.asStateFlow()

    private val _dateString = MutableStateFlow("SUN. 24 MAY. 2026")
    val dateString: StateFlow<String> = _dateString.asStateFlow()

    private val _inputDigits = MutableStateFlow("")
    val inputDigits: StateFlow<String> = _inputDigits.asStateFlow()

    // Unlock sequence state
    private val _unlockProgress = MutableStateFlow(0f)
    val unlockProgress: StateFlow<Float> = _unlockProgress.asStateFlow()

    private val _unlockStageMessage = MutableStateFlow("DECRYPTING INTERFACE...")
    val unlockStageMessage: StateFlow<String> = _unlockStageMessage.asStateFlow()

    private val _unlockSequenceState = MutableStateFlow("BLACK_OUT") // "BLACK_OUT", "LOADING", "GLITCH_WELCOME", "DONE"
    val unlockSequenceState: StateFlow<String> = _unlockSequenceState.asStateFlow()

    // Gemini AI Terminal Chat State
    private val _chatHistory = MutableStateFlow<List<Content>>(emptyList())
    val chatHistory: StateFlow<List<Content>> = _chatHistory.asStateFlow()

    private val _chatUiMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatUiMessages: StateFlow<List<ChatMessage>> = _chatUiMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Focus Pomodoro Timer
    private val _pomodoroSecondsLeft = MutableStateFlow(1500) // 25 Min
    val pomodoroSecondsLeft: StateFlow<Int> = _pomodoroSecondsLeft.asStateFlow()

    private val _pomodoroIsRunning = MutableStateFlow(false)
    val pomodoroIsRunning: StateFlow<Boolean> = _pomodoroIsRunning.asStateFlow()

    private var pomodoroJob: Job? = null

    // Free Fire stats tracker
    private val _gNick = MutableStateFlow("Gamersito")
    val gNick: StateFlow<String> = _gNick.asStateFlow()

    private val _gID = MutableStateFlow("944040445")
    val gID: StateFlow<String> = _gID.asStateFlow()

    private val _gLevel = MutableStateFlow("72")
    val gLevel: StateFlow<String> = _gLevel.asStateFlow()

    private val _gRank = MutableStateFlow("Gran Maestro")
    val gRank: StateFlow<String> = _gRank.asStateFlow()

    private val _gHsRate = MutableStateFlow("68.4%")
    val gHsRate: StateFlow<String> = _gHsRate.asStateFlow()

    private val _gWinRate = MutableStateFlow("76.1%")
    val gWinRate: StateFlow<String> = _gWinRate.asStateFlow()

    // Selected Info Tab Subcategory and Person focus
    private val _activeInfoCategory = MutableStateFlow(InfoCategory.ALL)
    val activeInfoCategory: StateFlow<InfoCategory> = _activeInfoCategory.asStateFlow()

    private val _focusedPerson = MutableStateFlow<PersonProfile?>(null)
    val focusedPerson: StateFlow<PersonProfile?> = _focusedPerson.asStateFlow()

    // Active sub system for tools and gaming
    private val _activeToolSystem = MutableStateFlow(ToolSystem.MENU)
    val activeToolSystem: StateFlow<ToolSystem> = _activeToolSystem.asStateFlow()

    private val _activeGamingSystem = MutableStateFlow(GamingSystem.MENU)
    val activeGamingSystem: StateFlow<GamingSystem> = _activeGamingSystem.asStateFlow()

    // Simulated community chats in gamer section
    private val _simulatedEsportsFeed = MutableStateFlow<List<EsportsLog>>(emptyList())
    val simulatedEsportsFeed: StateFlow<List<EsportsLog>> = _simulatedEsportsFeed.asStateFlow()

    init {
        startClockUpdates()
        loadInitialLogs()
    }

    private fun startClockUpdates() {
        viewModelScope.launch {
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val dateFormat = SimpleDateFormat("EEE, dd MMM. yyyy", Locale.US)
            while (true) {
                val cal = Calendar.getInstance()
                _timeString.value = timeFormat.format(cal.time)
                _dateString.value = dateFormat.format(cal.time).uppercase(Locale.US)
                delay(1000)
            }
        }
    }

    fun onClockKeyPress(digit: String) {
        viewModelScope.launch {
            SoundSynthesizer.playTick()
            val current = _inputDigits.value
            val allowedDigits = if (digit == ":") ":" else digit.filter { it.isDigit() }
            if (digit.isNotEmpty() && allowedDigits.isEmpty()) return@launch

            val combined = current + allowedDigits
            val next = if (combined.length > 4) combined.takeLast(4) else combined
            _inputDigits.value = next

            val normalized = next.replace(":", "")
            if (normalized == "777") {
                triggerUnlockSequence()
            }
        }
    }

    fun clearClockInput() {
        viewModelScope.launch {
            SoundSynthesizer.playTick()
            _inputDigits.value = ""
        }
    }

    private fun triggerUnlockSequence() {
        _currentScreen.value = Screen.Unlocking
        _inputDigits.value = ""
        viewModelScope.launch {
            _unlockSequenceState.value = "BLACK_OUT"
            delay(1000)

            viewModelScope.launch { SoundSynthesizer.playSciFiPulse() }

            _unlockSequenceState.value = "LOADING"
            _unlockProgress.value = 0f
            val stages = listOf(
                "DECRYPTION CODE CHECK..." to 0.20f,
                "VPN LINKING..." to 0.45f,
                "DATABASE ALLOCATING..." to 0.70f,
                "Glitch Kernel Compiled..." to 0.90f,
                "SYSTEM ACTIVE_..." to 1.0f
            )
            for ((msg, progress) in stages) {
                _unlockStageMessage.value = msg
                val step = _unlockProgress.value
                val diff = progress - step
                for (j in 1..4) {
                    _unlockProgress.value = step + (diff * j / 4f)
                    delay(120)
                }
                SoundSynthesizer.playTick()
            }

            // Seleccionar frase para dar bienvenida
            rotateGreetingPhrase()

            _unlockSequenceState.value = "GLITCH_WELCOME"
            delay(3200)

            // Finalize shell state first to guarantee seamless first composition pass
            _activeSubScreen.value = SubScreen.NOTAS
            _unlockSequenceState.value = "DONE"
            _currentScreen.value = Screen.MainTerminal
            
            // Postear notificación al cargar
            postNotification("SISTEMA PROTOCOLO XL7 CONECTADO")
        }
    }

    fun lockSystem() {
        viewModelScope.launch {
            SoundSynthesizer.playError()
            _currentScreen.value = Screen.Clock
            _inputDigits.value = ""
            _activeSubScreen.value = SubScreen.NOTAS
            _activeToolSystem.value = ToolSystem.MENU
            _activeGamingSystem.value = GamingSystem.MENU
            
            // Elegir nueva frase para la pantalla de bloqueo
            rotateGreetingPhrase()
        }
    }

    fun navigateToSubScreen(sub: SubScreen) {
        viewModelScope.launch {
            SoundSynthesizer.playTick()
            _activeSubScreen.value = sub
        }
    }

    fun createNote(title: String, content: String, type: String) {
        viewModelScope.launch {
            SoundSynthesizer.playConfirm()
            val note = Note(title = title, content = content, noteType = type)
            noteRepository.insert(note)
            postNotification("NUEVO REGISTRO GUARDADO EXIGIDO")
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            SoundSynthesizer.playError()
            noteRepository.delete(note)
            postNotification("REPORTE EXPURGADO CON EXITO")
        }
    }

    fun sendAiMessage(messageText: String) {
        if (messageText.isBlank() || _isAiLoading.value) return
        viewModelScope.launch {
            SoundSynthesizer.playTick()
            val newUserMsg = ChatMessage(text = messageText, isUser = true)
            _chatUiMessages.value = _chatUiMessages.value + newUserMsg
            _isAiLoading.value = true

            val historyList = _chatHistory.value
            val responseText = GeminiClient.chatWithGemini(messageText, historyList)

            _isAiLoading.value = false
            SoundSynthesizer.playConfirm()

            val newAiMsg = ChatMessage(text = responseText, isUser = false)
            _chatUiMessages.value = _chatUiMessages.value + newAiMsg

            _chatHistory.value = historyList + listOf(
                Content(parts = listOf(Part(text = messageText))),
                Content(parts = listOf(Part(text = responseText)))
            )
        }
    }

    fun clearAiChat() {
        viewModelScope.launch {
            SoundSynthesizer.playError()
            _chatHistory.value = emptyList()
            _chatUiMessages.value = emptyList()
            postNotification("HISTORIAL DE CHAT EXPURGADO")
        }
    }

    fun togglePomodoro() {
        viewModelScope.launch {
            SoundSynthesizer.playTick()
            if (_pomodoroIsRunning.value) {
                stopPomodoro()
                postNotification("CRONOMETRO ENFOQUE DETENIDO")
            } else {
                startPomodoro()
                postNotification("MODO ENFOQUE ACTIVADO")
            }
        }
    }

    private fun startPomodoro() {
        _pomodoroIsRunning.value = true
        pomodoroJob = viewModelScope.launch {
            while (_pomodoroSecondsLeft.value > 0) {
                delay(1000)
                _pomodoroSecondsLeft.value -= 1
            }
            _pomodoroIsRunning.value = false
            try {
                SoundSynthesizer.playSciFiPulse()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _pomodoroSecondsLeft.value = 1500
            postNotification("CICLO DE ENFOQUE COMPLETADO")
        }
    }

    fun resetPomodoro() {
        viewModelScope.launch {
            SoundSynthesizer.playError()
            stopPomodoro()
            _pomodoroSecondsLeft.value = 1500
            postNotification("CRONOMETRO ENFOQUE REESTABLECIDO")
        }
    }

    private fun stopPomodoro() {
        _pomodoroIsRunning.value = false
        pomodoroJob?.cancel()
    }

    fun selectTool(tool: ToolSystem) {
        viewModelScope.launch {
            SoundSynthesizer.playTick()
            _activeToolSystem.value = tool
        }
    }

    fun selectGaming(sys: GamingSystem) {
        viewModelScope.launch {
            SoundSynthesizer.playTick()
            _activeGamingSystem.value = sys
        }
    }

    fun saveFreeFireStats(nick: String, id: String, lv: String, rank: String, hs: String, wr: String) {
        viewModelScope.launch {
            SoundSynthesizer.playConfirm()
            _gNick.value = nick
            _gID.value = id
            _gLevel.value = lv
            _gRank.value = rank
            _gHsRate.value = hs
            _gWinRate.value = wr
        }
    }

    fun selectInfoCategory(category: InfoCategory) {
        viewModelScope.launch {
            SoundSynthesizer.playTick()
            _activeInfoCategory.value = category
            _focusedPerson.value = null
        }
    }

    fun focusPerson(person: PersonProfile?) {
        viewModelScope.launch {
            SoundSynthesizer.playConfirm()
            _focusedPerson.value = person
        }
    }

    fun postEsportsLog(text: String) {
        viewModelScope.launch {
            SoundSynthesizer.playConfirm()
            val newLog = EsportsLog("Tú (Agente)", text, "Ahora")
            _simulatedEsportsFeed.value = _simulatedEsportsFeed.value + newLog
        }
    }

    private fun loadInitialLogs() {
        _simulatedEsportsFeed.value = listOf(
            EsportsLog("System-XL7", "Canal de reclutamiento activo. Protocolos listos.", "Ayer"),
            EsportsLog("Gamersito", "Scrim privada completada con éxito. Guardando logs.", "Hace 3 horas"),
            EsportsLog("Nexus-9", "Algoritmo de sensibilidades móviles afinado para Free fire.", "Hace 1 hora"),
            EsportsLog("CyberClips", "Compilación de clips finales lista para render.", "Hace 30 min")
        )
    }
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class EsportsLog(
    val sender: String,
    val text: String,
    val time: String
)

data class PersonProfile(
    val name: String,
    val avatarType: Int,
    val role: String,
    val status: String,
    val goals: String,
    val notes: String,
    val relation: String
)
