package com.example.data.model

data class VoiceCommand(
    val id: String,
    val title: String,
    val command: String,
    val category: CommandCategory,
    val response: String,
    val actionType: ActionType
)

enum class CommandCategory(val label: String) {
    PHONE_CONTROL("Device & System"),
    WEB_AND_MARKET("Live Search & Markets"),
    VISION_SCREEN("Screen Perception"),
    CODING_AGENT("Bypass IDE Coding")
}

enum class ActionType {
    WHO_CREATED_YOU,
    DOWNLOAD_APP,
    CHANGE_BRIGHTNESS,
    SCROLL_SCREEN,
    REALTIME_MARKET,
    LIVE_NEWS,
    SCREEN_UNDERSTANDING,
    CODE_SYNTHESIS,
    AUTO_FIX_BUGS,
    LOCALHOST_PREVIEW
}

data class AssistantVoiceMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val actionBadge: String? = null,
    val payloadCode: String? = null
)

data class MariaAssistantState(
    val isListening: Boolean = false,
    val isSpeaking: Boolean = false,
    val isVoiceMuted: Boolean = false,
    val alwaysOnMode: Boolean = true,
    val backgroundAssistantActive: Boolean = true,
    val liveSearchEnabled: Boolean = true,
    val brightnessPercent: Int = 75,
    val screenAnalysisStatus: String = "Monitoring active viewport...",
    val latestNews: String = "PM Modi inaugurates Next-Gen AI & Semiconductor Tech Hub in India; Global tech alliances formed.",
    val marketInfo: String = "Vodafone Idea (IDEA) ₹7.82 (+3.4%), NIFTY 50 25,840 (+0.8%). Strong momentum in Telecom & Tech sectors."
)
