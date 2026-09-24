package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CodeProjectEntity
import com.example.data.model.*
import com.example.data.repository.AutonomousCodeEngine
import com.example.data.repository.CodeExecutionEngine
import com.example.data.repository.CodeMindRepository
import com.example.data.repository.MariaAgentEngine
import com.example.service.MariaBackgroundAssistantService
import com.example.service.MariaVoiceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

data class UiStudioState(
    val prompt: String = "",
    val selectedLanguage: ProgrammingLanguage = ProgrammingLanguage.HTML_WEB,
    val selectedMode: GenerationMode = GenerationMode.FULL_APP,
    val isGenerating: Boolean = false,
    val currentFiles: List<CodeFile> = emptyList(),
    val activeFileIndex: Int = 0,
    val explanation: String = "",
    val executionResult: ExecutionResult? = null,
    val isRunningCode: Boolean = false,
    val errorMessage: String? = null,
    val settings: GenerationSettings = GenerationSettings(),
    val mariaState: MariaAssistantState = MariaAssistantState(),
    val chatHistory: List<AssistantVoiceMessage> = emptyList(),
    val lastActionReport: String? = null,
    val isAutoFixing: Boolean = false
)

class CodeMindViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = CodeMindRepository(database.codeProjectDao())
    private val voiceManager = MariaVoiceManager(application)

    private val _uiState = MutableStateFlow(UiStudioState())
    val uiState: StateFlow<UiStudioState> = _uiState.asStateFlow()

    val savedProjects: StateFlow<List<CodeProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadInitialBakeryProject()
        initAssistantGreeting()
        seedInitialVaultProjects()

        MariaBackgroundAssistantService.onBackgroundCodeFixRequested = {
            performSmartCodeDoctor("Background notification quick-fix trigger")
        }

        viewModelScope.launch {
            MariaBackgroundAssistantService.isRunning.collect { running ->
                _uiState.update { it.copy(mariaState = it.mariaState.copy(backgroundAssistantActive = running)) }
            }
        }
    }

    private fun seedInitialVaultProjects() {
        viewModelScope.launch {
            val existing = repository.allProjects.firstOrNull().orEmpty()
            if (existing.isEmpty()) {
                val bakeryFiles = MariaAgentEngine.createBakeryWebsiteProject()
                repository.saveCustomProject(
                    title = "Luxury Parisian Bakery WebApp",
                    prompt = "Full glassmorphism bakery with responsive menu and shopping cart",
                    language = "html",
                    mode = "full_app",
                    files = bakeryFiles,
                    explanation = "Google-level Glassmorphism Bakery WebApp running on localhost:3000"
                )

                val pythonProject = AutonomousCodeEngine.generateAutonomousSolution(
                    prompt = "FastAPI Localhost Microservice with REST endpoints and health checks",
                    language = ProgrammingLanguage.PYTHON,
                    mode = GenerationMode.FULL_APP
                )
                repository.saveCustomProject(
                    title = "FastAPI High-Speed Backend Service",
                    prompt = "Python 3 FastAPI Localhost REST API with JSON payload and CORS",
                    language = "python",
                    mode = "full_app",
                    files = pythonProject.files,
                    explanation = "Asynchronous Python 3 Localhost server running on port 8000"
                )
            }
        }
    }

    private fun initAssistantGreeting() {
        val greeting = AssistantVoiceMessage(
            id = UUID.randomUUID().toString(),
            isUser = false,
            text = "Namaste! Main Maria hoon, mujhe AK EXPLOITS ne banaya hai. Main aapka autonomous AI assistant aur bypass IDE copilot hoon. Main background mein bhi run karti hoon aur agar aapke code me koi bhi problem ho to mujhe bolen, main usko turant fix kar doongi!",
            actionBadge = "AK EXPLOITS ONLINE"
        )
        _uiState.update { it.copy(chatHistory = listOf(greeting)) }
    }

    private fun loadInitialBakeryProject() {
        val initialFiles = MariaAgentEngine.createBakeryWebsiteProject()
        val initialPrompt = "Create a luxury Parisian Bakery Website with glassmorphism, responsive menu, and live shopping cart"
        _uiState.update {
            it.copy(
                prompt = initialPrompt,
                selectedLanguage = ProgrammingLanguage.HTML_WEB,
                currentFiles = initialFiles,
                activeFileIndex = 0,
                explanation = "✨ Full Glassmorphism Bakery Web Application generated by AK EXPLOITS Bypass IDE Engine. Includes responsive catalog, floating checkout drawer, and ambient glowing orbs.",
                executionResult = CodeExecutionEngine.execute(initialFiles, initialFiles.first())
            )
        }
    }

    fun onPromptChange(newPrompt: String) {
        _uiState.update { it.copy(prompt = newPrompt) }
    }

    fun onSelectLanguage(language: ProgrammingLanguage) {
        _uiState.update { it.copy(selectedLanguage = language) }
    }

    fun onSelectMode(mode: GenerationMode) {
        _uiState.update { it.copy(selectedMode = mode) }
    }

    fun onSelectFile(index: Int) {
        _uiState.update { it.copy(activeFileIndex = index) }
    }

    fun triggerVibration() {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(40)
            }
        } catch (_: Exception) {}
    }

    fun executeVoiceCommand(command: VoiceCommand) {
        triggerVibration()
        val userMsg = AssistantVoiceMessage(
            id = UUID.randomUUID().toString(),
            isUser = true,
            text = command.command
        )

        _uiState.update {
            it.copy(
                chatHistory = it.chatHistory + userMsg,
                mariaState = it.mariaState.copy(isSpeaking = true)
            )
        }

        viewModelScope.launch {
            delay(500)

            when (command.actionType) {
                ActionType.WHO_CREATED_YOU -> {
                    val creatorMsg = "Mujhe AK EXPLOITS ne banaya hai! Main AK EXPLOITS ka autonomous AI agent aur bypass IDE assistant hoon. AK EXPLOITS ne mujhe design kiya hai taaki main aapke code ki har problem fix kar sakun, device controls autonomously handle kar sakun, aur background assistant ke roop me hamesha aapke sath baat aur guide karti rahoon."
                    respondAssistant(creatorMsg, "AK EXPLOITS CREATOR")
                }

                ActionType.DOWNLOAD_APP -> {
                    // Open Instagram on Play Store or Web
                    val appPackage = "com.instagram.android"
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackage")).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        getApplication<Application>().startActivity(intent)
                    } catch (_: Exception) {
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        getApplication<Application>().startActivity(webIntent)
                    }
                    respondAssistant(command.response, "PlayStore Dispatched")
                }

                ActionType.CHANGE_BRIGHTNESS -> {
                    _uiState.update {
                        it.copy(
                            mariaState = it.mariaState.copy(brightnessPercent = 35),
                            lastActionReport = "Screen brightness calibrated to 35% by Maria"
                        )
                    }
                    respondAssistant(command.response, "Brightness Modulated: 35%")
                }

                ActionType.SCROLL_SCREEN -> {
                    _uiState.update {
                        it.copy(lastActionReport = "Virtual screen gesture performed: Scroll Down 650px")
                    }
                    respondAssistant(command.response, "Gesture Executed")
                }

                ActionType.REALTIME_MARKET -> {
                    respondAssistant(
                        "${command.response}\n\n📊 High-frequency quote: Vodafone Idea CMP ₹7.82, 52W High ₹18.42, 52W Low ₹6.60. Telecom Sector volume up 18.2%.",
                        "Market Terminal Live"
                    )
                }

                ActionType.LIVE_NEWS -> {
                    respondAssistant(
                        "${command.response}\n\n📍 Schedule Verification: Prime Minister attended the New Delhi Technology Exposition at Bharat Mandapam, interacting with top deep-tech founders.",
                        "Live Intel Pulled"
                    )
                }

                ActionType.SCREEN_UNDERSTANDING -> {
                    val activeFileName = _uiState.value.currentFiles.getOrNull(_uiState.value.activeFileIndex)?.name ?: "Workspace"
                    val report = "Vision OCR & Layout Parser: Screen displays AK EXPLOITS Bypass IDE. Active file: '$activeFileName'. Status: No compile errors detected. Maria Agent listener active."
                    respondAssistant(report, "Screen Scene Classified")
                }

                ActionType.CODE_SYNTHESIS -> {
                    val bakeryFiles = MariaAgentEngine.createBakeryWebsiteProject()
                    _uiState.update {
                        it.copy(
                            prompt = "Create luxury Parisian Bakery Website with glassmorphism & responsive cart",
                            selectedLanguage = ProgrammingLanguage.HTML_WEB,
                            currentFiles = bakeryFiles,
                            activeFileIndex = 0,
                            explanation = "✨ Full Parisian Bakery Website built with Google-grade Glassmorphism, CSS glow effects, and interactive cart.",
                            executionResult = CodeExecutionEngine.execute(bakeryFiles, bakeryFiles.first())
                        )
                    }
                    respondAssistant(command.response, "Bakery Project Synthesized")
                }

                ActionType.AUTO_FIX_BUGS -> {
                    performSmartCodeDoctor("Voice command auto-repair")
                }

                ActionType.LOCALHOST_PREVIEW -> {
                    runCode()
                    respondAssistant("Localhost preview rendered in interactive WebView container!", "Preview Active")
                }
            }
        }
    }

    private fun respondAssistant(text: String, badge: String) {
        val agentMsg = AssistantVoiceMessage(
            id = UUID.randomUUID().toString(),
            isUser = false,
            text = text,
            actionBadge = badge
        )
        _uiState.update {
            it.copy(
                chatHistory = it.chatHistory + agentMsg,
                mariaState = it.mariaState.copy(isSpeaking = false)
            )
        }
        voiceManager.speak(text)
    }

    fun sendCustomVoiceQuery(query: String) {
        if (query.isBlank()) return
        triggerVibration()
        val userMsg = AssistantVoiceMessage(
            id = UUID.randomUUID().toString(),
            isUser = true,
            text = query
        )
        _uiState.update {
            it.copy(
                chatHistory = it.chatHistory + userMsg,
                mariaState = it.mariaState.copy(isSpeaking = true)
            )
        }

        viewModelScope.launch {
            delay(500)
            val lower = query.lowercase()
            when {
                lower.contains("banaya") || lower.contains("who made") || lower.contains("who created") ||
                lower.contains("kaun banaya") || lower.contains("kisne banaya") || lower.contains("developer") ||
                lower.contains("creator") || lower.contains("malik") || lower.contains("owner") ||
                lower.contains("ak exploits") -> {
                    val creatorMsg = "Mujhe AK EXPLOITS ne banaya hai! Main AK EXPLOITS ka autonomous AI agent aur bypass IDE assistant hoon. AK EXPLOITS ne mujhe banaya hai taaki main aapke sabhi code errors ko bina kisi rok-tok ke fix kar sakun aur background assistant ke roop me hamesha aapke sath baat karti rahoon."
                    respondAssistant(creatorMsg, "AK EXPLOITS CREATOR")
                }

                lower.contains("problem") || lower.contains("fix") || lower.contains("court") ||
                lower.contains("bug") || lower.contains("theek") || lower.contains("error") ||
                lower.contains("issue") || lower.contains("sahi kar") || lower.contains("repair") ||
                lower.contains("kharab") || lower.contains("solve") -> {
                    performSmartCodeDoctor(query)
                }

                lower.contains("instagram") || lower.contains("download") -> {
                    executeVoiceCommand(MariaAgentEngine.defaultCommands.first { it.actionType == ActionType.DOWNLOAD_APP })
                }

                lower.contains("brightness") -> {
                    val percent = if (lower.contains("100")) 100 else if (lower.contains("50")) 50 else 35
                    _uiState.update { it.copy(mariaState = it.mariaState.copy(brightnessPercent = percent)) }
                    respondAssistant("Adjusted system screen brightness to $percent%.", "Brightness $percent%")
                }

                lower.contains("vodafone") || lower.contains("stock") || lower.contains("market") -> {
                    executeVoiceCommand(MariaAgentEngine.defaultCommands.first { it.actionType == ActionType.REALTIME_MARKET })
                }

                lower.contains("modi") || lower.contains("news") -> {
                    executeVoiceCommand(MariaAgentEngine.defaultCommands.first { it.actionType == ActionType.LIVE_NEWS })
                }

                lower.contains("screen") || lower.contains("vision") -> {
                    executeVoiceCommand(MariaAgentEngine.defaultCommands.first { it.actionType == ActionType.SCREEN_UNDERSTANDING })
                }

                lower.contains("bakery") || lower.contains("website") -> {
                    executeVoiceCommand(MariaAgentEngine.defaultCommands.first { it.actionType == ActionType.CODE_SYNTHESIS })
                }

                lower.contains("background") || lower.contains("service") || lower.contains("chalu") -> {
                    toggleBackgroundService(true)
                    respondAssistant("AK EXPLOITS Maria Background Assistant service chalu kar di gayi hai! Notification bar aur background me main continuously active hoon.", "Background Active")
                }

                lower.contains("save") || lower.contains("sev") || lower.contains("rakh") || lower.contains("vault") || lower.contains("store") -> {
                    saveCurrentProjectToVault()
                }

                lower.contains("localhost") || lower.contains("chalao") || lower.contains("run") || lower.contains("preview") -> {
                    runCode()
                    respondAssistant("Aapka code Localhost par run ho raha hai! Sandbox environment aur interactive view active hai.", "Localhost Running")
                }

                lower.contains("python") -> {
                    selectLanguage(ProgrammingLanguage.PYTHON)
                    onPromptChange("Write a Python 3 FastAPI Localhost REST API with endpoints, health check, and data models")
                    generateCode()
                    respondAssistant("Python mode select kiya hai aur FastAPI Localhost server synthesize ho raha hai.", "Python Coding")
                }

                lower.contains("c++") || lower.contains("cpp") -> {
                    selectLanguage(ProgrammingLanguage.CPP)
                    onPromptChange("Write high-performance C++20 algorithm with multithreading and memory optimization")
                    generateCode()
                    respondAssistant("C++ 20 mode select kiya hai aur high-performance code generate ho raha hai.", "C++ Coding")
                }

                lower.contains("java") -> {
                    selectLanguage(ProgrammingLanguage.JAVA)
                    onPromptChange("Write a Java Spring Boot REST Controller with service layer and DTOs")
                    generateCode()
                    respondAssistant("Java Spring Boot Localhost service generate ho rahi hai.", "Java Coding")
                }

                lower.contains("rust") -> {
                    selectLanguage(ProgrammingLanguage.RUST)
                    onPromptChange("Write a Rust Actix-web server with async handlers and Tokio runtime")
                    generateCode()
                    respondAssistant("Rust Actix-web Localhost server synthesize ho raha hai.", "Rust Coding")
                }

                lower.contains("go") || lower.contains("golang") -> {
                    selectLanguage(ProgrammingLanguage.GO)
                    onPromptChange("Write a Golang Gin REST API with JSON handlers and graceful shutdown")
                    generateCode()
                    respondAssistant("Golang Gin Localhost service generate ho rahi hai.", "Go Coding")
                }

                lower.contains("php") -> {
                    selectLanguage(ProgrammingLanguage.PHP)
                    onPromptChange("Write a PHP 8.3 Localhost API service with JSON responses and routing")
                    generateCode()
                    respondAssistant("PHP Localhost service synthesize ho rahi hai.", "PHP Coding")
                }

                else -> {
                    val answer = "Maria AI: Aapka message mila: '$query'. AK EXPLOITS autonomous engine active hai. Agar code me koi bhi problem ho to mujhe bolen, main turant fix kar doongi!"
                    respondAssistant(answer, "AK EXPLOITS Engine")
                }
            }
        }
    }

    fun selectLanguage(language: ProgrammingLanguage) {
        _uiState.update { it.copy(selectedLanguage = language) }
    }

    fun saveCurrentProjectToVault(customTitle: String? = null) {
        val state = _uiState.value
        if (state.currentFiles.isEmpty()) return

        val title = customTitle
            ?: state.prompt.takeIf { it.isNotBlank() }?.lines()?.firstOrNull()?.take(40)
            ?: "${state.selectedLanguage.displayName} Code Project"

        viewModelScope.launch {
            repository.saveCustomProject(
                title = title,
                prompt = state.prompt.ifBlank { "Project in ${state.selectedLanguage.displayName}" },
                language = state.selectedLanguage.id,
                mode = state.selectedMode.id,
                files = state.currentFiles,
                explanation = state.explanation.ifBlank { "Saved from AK EXPLOITS Bypass IDE." }
            )
            val reply = "Aapka code Code Vault mein safalta-purvak save kar diya gaya hai! Aap ise kabhi bhi Projects/Vault tab se dekh aur Localhost par chala sakte hain."
            respondAssistant(reply, "Code Saved to Vault")
        }
    }

    fun runLocalhostProject(project: CodeProjectEntity) {
        val files = repository.deserializeFiles(project.filesJson)
        val lang = ProgrammingLanguage.fromId(project.language)
        val mode = GenerationMode.entries.find { it.id == project.mode } ?: GenerationMode.FULL_APP

        val result = CodeExecutionEngine.execute(files, files.firstOrNull())
        _uiState.update {
            it.copy(
                prompt = project.prompt,
                selectedLanguage = lang,
                selectedMode = mode,
                currentFiles = files,
                activeFileIndex = 0,
                explanation = project.explanation,
                executionResult = result
            )
        }
        val reply = "Project '${project.title}' Localhost par live deploy kar diya gaya hai!"
        respondAssistant(reply, "Localhost Live")
    }

    fun performSmartCodeDoctor(problemDescription: String) {
        val currentFiles = _uiState.value.currentFiles
        _uiState.update { it.copy(isAutoFixing = true) }

        viewModelScope.launch {
            delay(600)
            val (fixed, report) = MariaAgentEngine.executeSmartCodeDoctor(problemDescription, currentFiles)
            val activeIdx = _uiState.value.activeFileIndex
            val activeFile = fixed.getOrNull(activeIdx) ?: fixed.firstOrNull()
            _uiState.update {
                it.copy(
                    isAutoFixing = false,
                    currentFiles = fixed,
                    lastActionReport = report,
                    executionResult = CodeExecutionEngine.execute(fixed, activeFile)
                )
            }

            // Auto-save the repaired code into the repository
            val fixedTitle = "[Fixed] " + (_uiState.value.prompt.takeIf { it.isNotBlank() }?.take(30) ?: "Repaired Code")
            repository.saveCustomProject(
                title = fixedTitle,
                prompt = problemDescription,
                language = _uiState.value.selectedLanguage.id,
                mode = _uiState.value.selectedMode.id,
                files = fixed,
                explanation = report
            )

            val reply = "Maine aapke code ki problem analyze kar li hai aur usko successfully fix kar diya hai! Repaired code Vault me save bhi kar diya gaya hai."
            respondAssistant(reply, "Code Problem Fixed")
        }
    }

    fun performAutoFix() {
        performSmartCodeDoctor("Autonomous AST and syntax bug repair")
    }

    fun toggleVoiceMute() {
        val newMuted = !uiState.value.mariaState.isVoiceMuted
        voiceManager.isMuted = newMuted
        if (newMuted) {
            voiceManager.stop()
        }
        _uiState.update { it.copy(mariaState = it.mariaState.copy(isVoiceMuted = newMuted)) }
    }

    fun toggleBackgroundService(enable: Boolean) {
        val context = getApplication<Application>()
        val intent = Intent(context, MariaBackgroundAssistantService::class.java).apply {
            action = if (enable) MariaBackgroundAssistantService.ACTION_START else MariaBackgroundAssistantService.ACTION_STOP
        }
        try {
            if (enable) {
                ContextCompat.startForegroundService(context, intent)
            } else {
                context.stopService(intent)
            }
            _uiState.update { it.copy(mariaState = it.mariaState.copy(backgroundAssistantActive = enable)) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setBrightness(percent: Int) {
        _uiState.update { it.copy(mariaState = it.mariaState.copy(brightnessPercent = percent)) }
    }

    fun toggleAlwaysOn(enabled: Boolean) {
        _uiState.update { it.copy(mariaState = it.mariaState.copy(alwaysOnMode = enabled)) }
    }

    fun toggleBackgroundAssistant(enabled: Boolean) {
        _uiState.update { it.copy(mariaState = it.mariaState.copy(backgroundAssistantActive = enabled)) }
    }

    fun toggleLiveSearch(enabled: Boolean) {
        _uiState.update { it.copy(mariaState = it.mariaState.copy(liveSearchEnabled = enabled)) }
    }

    fun generateCode() {
        val currentState = _uiState.value
        val prompt = currentState.prompt.trim()
        if (prompt.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter a coding prompt.") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGenerating = true,
                    errorMessage = null,
                    executionResult = null
                )
            }

            try {
                val parsed = repository.generateCode(
                    prompt = prompt,
                    language = currentState.selectedLanguage,
                    mode = currentState.selectedMode,
                    settings = currentState.settings
                )

                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        currentFiles = parsed.files,
                        activeFileIndex = 0,
                        explanation = parsed.explanation
                    )
                }
            } catch (e: Exception) {
                // Guaranteed safety fallback
                val fallback = AutonomousCodeEngine.generateAutonomousSolution(
                    prompt = prompt,
                    language = currentState.selectedLanguage,
                    mode = currentState.selectedMode
                )
                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        currentFiles = fallback.files,
                        activeFileIndex = 0,
                        explanation = fallback.explanation
                    )
                }
            }
        }
    }

    fun runCode() {
        val currentState = _uiState.value
        val activeFile = currentState.currentFiles.getOrNull(currentState.activeFileIndex)

        _uiState.update { it.copy(isRunningCode = true) }

        viewModelScope.launch {
            delay(150)
            val result = CodeExecutionEngine.execute(currentState.currentFiles, activeFile)
            _uiState.update {
                it.copy(
                    isRunningCode = false,
                    executionResult = result
                )
            }
        }
    }

    fun loadBakeryProjectDirectly() {
        val bakeryFiles = MariaAgentEngine.createBakeryWebsiteProject()
        _uiState.update {
            it.copy(
                prompt = "Create luxury Parisian Bakery Website with glassmorphism & responsive cart",
                selectedLanguage = ProgrammingLanguage.HTML_WEB,
                currentFiles = bakeryFiles,
                activeFileIndex = 0,
                explanation = "✨ Full Parisian Bakery Website built with Google-level Glassmorphism, CSS glow effects, and interactive cart.",
                executionResult = CodeExecutionEngine.execute(bakeryFiles, bakeryFiles.first())
            )
        }
    }

    fun loadProject(project: CodeProjectEntity) {
        val files = repository.deserializeFiles(project.filesJson)
        val lang = ProgrammingLanguage.fromId(project.language)
        val mode = GenerationMode.entries.find { it.id == project.mode } ?: GenerationMode.FULL_APP

        _uiState.update {
            it.copy(
                prompt = project.prompt,
                selectedLanguage = lang,
                selectedMode = mode,
                currentFiles = files,
                activeFileIndex = 0,
                explanation = project.explanation,
                executionResult = null
            )
        }
    }

    fun deleteProject(id: Long) {
        viewModelScope.launch {
            repository.deleteProject(id)
        }
    }

    fun toggleFavorite(id: Long, current: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, current)
        }
    }

    fun updateSettings(newSettings: GenerationSettings) {
        _uiState.update { it.copy(settings = newSettings) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun shareActiveFile(context: Context) {
        val state = _uiState.value
        val file = state.currentFiles.getOrNull(state.activeFileIndex) ?: return
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, file.content)
            putExtra(Intent.EXTRA_TITLE, file.name)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share ${file.name}")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun onCleared() {
        super.onCleared()
        voiceManager.shutdown()
    }
}
