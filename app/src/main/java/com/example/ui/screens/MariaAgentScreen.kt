package com.example.ui.screens

import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AkExploitsEmblem
import com.example.ui.components.EmblemSize
import com.example.ui.theme.*
import com.example.ui.viewmodel.CodeMindViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MariaAgentScreen(
    viewModel: CodeMindViewModel,
    onNavigateToIDE: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    var inputText by remember { mutableStateOf("") }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spokenSpans = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = spokenSpans?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                viewModel.sendCustomVoiceQuery(spokenText)
            }
        }
    }

    val notifPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.toggleBackgroundService(true)
            Toast.makeText(context, "AK EXPLOITS Background Assistant Started!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.chatHistory.size) {
        if (uiState.chatHistory.isNotEmpty()) {
            listState.animateScrollToItem(uiState.chatHistory.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Ultra-Clean Header: AK EXPLOITS Emblem & Controls
        Surface(
            color = DarkSurface,
            border = BorderStroke(1.dp, CrimsonBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AkExploitsEmblem(
                        size = EmblemSize.SMALL,
                        onClick = {
                            viewModel.triggerVibration()
                            Toast.makeText(context, "AK EXPLOITS • Maria Assistant Online", Toast.LENGTH_SHORT).show()
                        }
                    )

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "AK EXPLOITS",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary,
                                letterSpacing = 0.5.sp
                            )
                            Surface(
                                color = BloodCrimson.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.6f)),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "AI MARIA",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = BloodCrimson,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (uiState.mariaState.backgroundAssistantActive) TerminalEmerald else NeonCyan)
                            )
                            Text(
                                text = if (uiState.mariaState.backgroundAssistantActive) "BG Active • Gemini 3.8" else "Standby • Gemini 3.8",
                                fontSize = 10.sp,
                                color = if (uiState.mariaState.backgroundAssistantActive) TerminalEmerald else NeonCyan
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Background Service Toggle Pill
                    FilledTonalButton(
                        onClick = {
                            viewModel.triggerVibration()
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                                    notifPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    viewModel.toggleBackgroundService(!uiState.mariaState.backgroundAssistantActive)
                                }
                            } else {
                                viewModel.toggleBackgroundService(!uiState.mariaState.backgroundAssistantActive)
                            }
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (uiState.mariaState.backgroundAssistantActive) BloodCrimson.copy(alpha = 0.25f) else DarkSurfaceElevated,
                            contentColor = if (uiState.mariaState.backgroundAssistantActive) Color.White else TextSecondary
                        ),
                        border = BorderStroke(1.dp, if (uiState.mariaState.backgroundAssistantActive) BloodCrimson else DarkBorder),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("btn_bg_service_pill")
                    ) {
                        Text(
                            text = if (uiState.mariaState.backgroundAssistantActive) "BG: ON" else "BG: OFF",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Voice Mute Toggle
                    IconButton(
                        onClick = {
                            viewModel.triggerVibration()
                            viewModel.toggleVoiceMute()
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_voice_mute_toggle")
                    ) {
                        Icon(
                            imageVector = if (uiState.mariaState.isVoiceMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = "Voice Mute",
                            tint = if (uiState.mariaState.isVoiceMuted) TextMuted else BloodCrimson,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Bypass IDE Shortcut
                    Button(
                        onClick = onNavigateToIDE,
                        colors = ButtonDefaults.buttonColors(containerColor = BloodCrimson),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("btn_switch_ide")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "IDE",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("IDE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Quick Suggestion Chips Carousel
        Surface(
            color = DarkSurfaceElevated,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Instant Code Doctor Chip
                AssistChip(
                    onClick = {
                        viewModel.triggerVibration()
                        viewModel.performSmartCodeDoctor("Mere code me problem hai, fix kar do")
                    },
                    label = { Text("⚡ Code Problem Doctor", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AutoFixHigh,
                            contentDescription = null,
                            tint = BloodCrimson,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = BloodCrimson.copy(alpha = 0.2f),
                        labelColor = TextPrimary
                    ),
                    border = BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.6f))
                )

                // Common Hindi/English Quick Action Chips
                val quickChips = listOf(
                    "Tumhe kisne banaya?",
                    "Localhost run karo",
                    "Python FastAPI server",
                    "Screen brightness 80%",
                    "Market news dikhao"
                )

                quickChips.forEach { prompt ->
                    SuggestionChip(
                        onClick = {
                            viewModel.triggerVibration()
                            viewModel.sendCustomVoiceQuery(prompt)
                        },
                        label = { Text(prompt, fontSize = 11.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = DarkSurfaceCard,
                            labelColor = TextSecondary
                        ),
                        border = BorderStroke(1.dp, DarkBorder)
                    )
                }
            }
        }

        // Chat & Agent Voice Timeline (Takes Full Weight)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.chatHistory, key = { it.id }) { msg ->
                if (msg.isUser) {
                    UserChatBubble(msg.text)
                } else {
                    AgentChatBubble(
                        message = msg.text,
                        badge = msg.actionBadge,
                        onOpenIDE = onNavigateToIDE
                    )
                }
            }

            if (uiState.mariaState.isSpeaking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = BloodCrimson,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Maria is speaking...",
                            fontSize = 11.sp,
                            color = BloodCrimson,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Bottom Input bar with Voice Mic Button & Quick Action
        Surface(
            color = DarkSurface,
            border = BorderStroke(1.dp, CrimsonBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_agent_command"),
                    placeholder = {
                        Text("Bol kar ya likh kar puche...", fontSize = 12.sp, color = TextMuted)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurfaceElevated,
                        unfocusedContainerColor = DarkSurfaceElevated,
                        focusedBorderColor = BloodCrimson,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Microphone Button (Voice Trigger)
                IconButton(
                    onClick = {
                        viewModel.triggerVibration()
                        try {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk to Maria (AK EXPLOITS Assistant)")
                            }
                            speechLauncher.launch(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, "Voice input simulation activated", Toast.LENGTH_SHORT).show()
                            viewModel.sendCustomVoiceQuery("Tumhe kisne banaya hai?")
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(BloodCrimson, CrimsonDeep)))
                        .border(BorderStroke(1.dp, BloodCrimson), CircleShape)
                        .testTag("btn_voice_mic")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Input",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Send Button
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendCustomVoiceQuery(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (inputText.isNotBlank()) BloodCrimson else DarkSurfaceElevated)
                        .testTag("btn_send_command")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank()) Color.White else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun UserChatBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            color = CrimsonDeep,
            border = BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun AgentChatBubble(
    message: String,
    badge: String?,
    onOpenIDE: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier.widthIn(max = 320.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (badge != null) {
                Surface(
                    color = BloodCrimson.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "● $badge",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = BloodCrimson,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Surface(
                color = DarkSurfaceCard,
                border = BorderStroke(1.dp, DarkBorder),
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = message,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    if (message.contains("Bakery") || message.contains("index.html") || message.contains("synthesized") || message.contains("Auto-Fix") || message.contains("Localhost")) {
                        Button(
                            onClick = onOpenIDE,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BloodCrimson),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Launch, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Open in Bypass IDE & Preview", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
