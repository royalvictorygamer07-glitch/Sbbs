package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AkExploitsEmblem
import com.example.ui.components.EmblemSize
import com.example.ui.theme.*
import com.example.ui.viewmodel.CodeMindViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: CodeMindViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var settings by remember(uiState.settings) { mutableStateOf(uiState.settings) }
    var showApiKey by remember { mutableStateOf(false) }

    val models = listOf(
        "gemini-2.5-flash" to "Gemini 3.8 / 2.5 Flash (Ultra-fast mobile coding & voice response)",
        "gemini-2.5-pro" to "Gemini 2.5 Pro (Deep architectural reasoning)",
        "gemini-1.5-flash" to "Gemini 1.5 Flash (Standard tier)"
    )

    val personas = listOf(
        "Maria - Autonomous Android AI Agent & Exploit Architect",
        "Senior Principal Systems Architect & Polyglot Hacker",
        "Algorithmic Grandmaster & Competitive Programmer",
        "Cybersecurity & Zero-Trust Defense Engineer"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card with AK EXPLOITS Emblem
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, CrimsonBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AkExploitsEmblem(
                    size = EmblemSize.LARGE,
                    onClick = {
                        viewModel.triggerVibration()
                        Toast.makeText(context, "AK EXPLOITS - DANGEROUS BYPASS ENGINE", Toast.LENGTH_SHORT).show()
                    }
                )

                Text(
                    text = "AK EXPLOITS",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "Autonomous Bypass IDE & Maria AI Copilot",
                    fontSize = 12.sp,
                    color = BloodCrimson,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Built for unrestricted, high-velocity code synthesis, device automation, and multi-file project execution.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }

        // Unrestricted Autonomy Card ("Koi bhi rok-tok na ho")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            border = BorderStroke(1.dp, if (settings.unrestrictedMode) BloodCrimson else DarkBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Unrestricted",
                            tint = BloodCrimson,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Unrestricted Coding Mode",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "Zero restrictions. Full autonomy code generation, full scripts, device control, and instant compilation without censorship.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Switch(
                    checked = settings.unrestrictedMode,
                    onCheckedChange = {
                        val updated = settings.copy(unrestrictedMode = it)
                        settings = updated
                        viewModel.updateSettings(updated)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = BloodCrimson,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = DarkSurface
                    ),
                    modifier = Modifier.testTag("switch_unrestricted")
                )
            }
        }

        // Model Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "SELECT AI MODEL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                models.forEach { (id, label) ->
                    val isSelected = settings.modelName == id
                    Surface(
                        onClick = {
                            val updated = settings.copy(modelName = id)
                            settings = updated
                            viewModel.updateSettings(updated)
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) CrimsonDeep.copy(alpha = 0.4f) else DarkSurfaceElevated,
                        border = if (isSelected) BorderStroke(1.dp, BloodCrimson) else null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    val updated = settings.copy(modelName = id)
                                    settings = updated
                                    viewModel.updateSettings(updated)
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = BloodCrimson,
                                    unselectedColor = TextMuted
                                )
                            )
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) TextPrimary else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Temperature Slider
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TEMPERATURE / CREATIVITY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "%.2f".format(settings.temperature),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BloodCrimson
                    )
                }

                Slider(
                    value = settings.temperature,
                    onValueChange = {
                        val updated = settings.copy(temperature = it)
                        settings = updated
                        viewModel.updateSettings(updated)
                    },
                    valueRange = 0.0f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = BloodCrimson,
                        activeTrackColor = CrimsonDeep,
                        inactiveTrackColor = DarkBorder
                    ),
                    modifier = Modifier.testTag("slider_temperature")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Precise (0.0)", fontSize = 10.sp, color = TextMuted)
                    Text("Ultra-Creative (1.0)", fontSize = 10.sp, color = TextMuted)
                }
            }
        }

        // Persona Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "AI ARCHITECT PERSONA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                personas.forEach { persona ->
                    val isSelected = settings.persona == persona
                    Surface(
                        onClick = {
                            val updated = settings.copy(persona = persona)
                            settings = updated
                            viewModel.updateSettings(updated)
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) CrimsonDeep.copy(alpha = 0.4f) else DarkSurfaceElevated,
                        border = if (isSelected) BorderStroke(1.dp, BloodCrimson) else null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    val updated = settings.copy(persona = persona)
                                    settings = updated
                                    viewModel.updateSettings(updated)
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = BloodCrimson,
                                    unselectedColor = TextMuted
                                )
                            )
                            Text(
                                text = persona,
                                fontSize = 12.sp,
                                color = if (isSelected) TextPrimary else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // API Key Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "GEMINI API KEY OVERRIDE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "By default, the server-side environment key is utilized automatically. You can optionally supply your own custom key.",
                    fontSize = 11.sp,
                    color = TextMuted
                )

                OutlinedTextField(
                    value = settings.customApiKey,
                    onValueChange = {
                        val updated = settings.copy(customApiKey = it)
                        settings = updated
                        viewModel.updateSettings(updated)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_api_key"),
                    placeholder = { Text("AIzaSy...", fontSize = 12.sp, color = TextMuted) },
                    visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showApiKey = !showApiKey }) {
                            Icon(
                                imageVector = if (showApiKey) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle key visibility",
                                tint = TextMuted
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurfaceElevated,
                        unfocusedContainerColor = DarkSurfaceElevated,
                        focusedBorderColor = BloodCrimson,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        }
    }
}
