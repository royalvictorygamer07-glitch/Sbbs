package com.example.data.model

data class CodeFile(
    val name: String,
    val language: String,
    val content: String
)

enum class GenerationMode(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconName: String
) {
    FULL_APP("full_app", "Full Architecture", "Complete multi-file production project", "FolderZip"),
    ALGORITHM("algorithm", "Algorithm & Logic", "High-performance DS, math & logic", "Psychology"),
    DEBUG_FIX("debug_fix", "Bug Fixer & Shield", "Deep bug analysis & security fixes", "BugReport"),
    OPTIMIZE("optimize", "Refactor & Speed", "Clean code, performance & Big-O optimization", "Speed"),
    TRANSLATOR("translator", "Cross Translator", "Translate code to any other language", "Translate"),
    UNRESTRICTED("unrestricted", "Omni Unrestricted", "Max autonomy, no limits, full power", "Bolt")
}

enum class ProgrammingLanguage(
    val id: String,
    val displayName: String,
    val extension: String,
    val iconColor: Long,
    val defaultPort: Int = 3000
) {
    HTML_WEB("html", "HTML5 & WebApp", ".html", 0xFFE34F26, 3000),
    JAVASCRIPT("javascript", "JavaScript (Node/Web)", ".js", 0xFFF7DF1E, 3000),
    TYPESCRIPT("typescript", "TypeScript (Fullstack)", ".ts", 0xFF3178C6, 3000),
    PYTHON("python", "Python 3 (FastAPI/Flask)", ".py", 0xFF3776AB, 8000),
    KOTLIN("kotlin", "Kotlin (Android/Ktor)", ".kt", 0xFF7F52FF, 8080),
    JAVA("java", "Java (Spring Boot)", ".java", 0xFFED8B00, 8080),
    CPP("cpp", "C++ 20", ".cpp", 0xFF00599C, 8080),
    C("c", "C (ANSI/C11)", ".c", 0xFF555555, 8080),
    RUST("rust", "Rust (Actix/Tokio)", ".rs", 0xFFDEA584, 8080),
    GO("go", "Golang (Gin/Fiber)", ".go", 0xFF00ADD8, 8080),
    PHP("php", "PHP / Laravel", ".php", 0xFF8892BF, 8000),
    SQL("sql", "SQL / Database", ".sql", 0xFFCC292B, 5432),
    SHELL("shell", "Bash / Shell", ".sh", 0xFF4EAA25, 22),
    DART("dart", "Dart / Flutter", ".dart", 0xFF0175C2, 8080),
    SWIFT("swift", "Swift (iOS/Vapor)", ".swift", 0xFFF05138, 8080),
    CSHARP("csharp", "C# (.NET Core)", ".cs", 0xFF239120, 5000),
    RUBY("ruby", "Ruby on Rails", ".rb", 0xFFCC342D, 3000),
    SOLIDITY("solidity", "Solidity (Web3)", ".sol", 0xFF363636, 8545);

    companion object {
        fun fromId(id: String): ProgrammingLanguage {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: HTML_WEB
        }
    }
}

data class GenerationSettings(
    val modelName: String = "gemini-2.5-flash", // Gemini Flash (high-speed mobile coding agent)
    val temperature: Float = 0.7f,
    val unrestrictedMode: Boolean = true,
    val customApiKey: String = "",
    val persona: String = "Senior Principal Systems Architect & Polyglot Hacker",
    val codeTheme: String = "Cyberpunk Neon",
    val glassmorphismEnabled: Boolean = true,
    val alwaysOnVoice: Boolean = true
)

data class ExecutionResult(
    val stdout: String,
    val stderr: String = "",
    val exitCode: Int = 0,
    val executionTimeMs: Long = 12,
    val isWebPreview: Boolean = false,
    val webHtml: String = "",
    val localhostUrl: String = "http://localhost:3000",
    val localhostPort: Int = 3000,
    val serverStatus: String = "ONLINE (200 OK)",
    val isBackendServer: Boolean = false,
    val apiResponseJson: String = ""
)
