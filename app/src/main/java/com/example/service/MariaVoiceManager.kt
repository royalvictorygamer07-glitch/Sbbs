package com.example.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class MariaVoiceManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    var isMuted: Boolean = false

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("MariaVoiceManager", "Error initializing TTS", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            // Attempt Hindi locale, fallback to US / Default
            val hindi = Locale("hi", "IN")
            val langResult = tts?.setLanguage(hindi)
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.US)
            }
            tts?.setSpeechRate(1.0f)
            tts?.setPitch(1.05f)

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }
            })
        } else {
            isInitialized = false
            Log.e("MariaVoiceManager", "TTS initialization failed: status=$status")
        }
    }

    fun speak(text: String) {
        if (isMuted || !isInitialized || text.isBlank()) return
        try {
            // Strip code blocks or symbols for cleaner speech
            val cleanText = text
                .replace(Regex("```[\\s\\S]*?```"), "Code block generated in editor.")
                .replace(Regex("[*#_`~]"), "")
                .take(300) // Keep concise voice utterance

            tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "MARIA_UTTERANCE_${System.currentTimeMillis()}")
        } catch (e: Exception) {
            Log.e("MariaVoiceManager", "TTS speak failed", e)
        }
    }

    fun stop() {
        try {
            tts?.stop()
            _isSpeaking.value = false
        } catch (_: Exception) {}
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
        } catch (_: Exception) {}
    }
}
