package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun DeviceControlScreen(
    viewModel: CodeMindViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title with AK EXPLOITS Emblem
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AkExploitsEmblem(size = EmblemSize.SMALL)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "AK EXPLOITS",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )
                        Surface(
                            color = BloodCrimson.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, BloodCrimson)
                        ) {
                            Text(
                                text = "BYPASS KERNEL",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = BloodCrimson,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Autonomous System Controller via Maria AI",
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                }
            }

            Surface(
                color = TerminalEmerald.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, TerminalEmerald)
            ) {
                Text(
                    text = "ACTIVE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = TerminalEmerald,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Action Report Banner if recent
        if (uiState.lastActionReport != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BloodCrimson)
                    Text(
                        text = uiState.lastActionReport ?: "",
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                }
            }
        }

        // Section: Display & Brightness Controls
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Brightness6, contentDescription = null, tint = AmberGlow)
                        Text("Screen Brightness Control", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    Text("${uiState.mariaState.brightnessPercent}%", fontSize = 14.sp, fontWeight = FontWeight.Black, color = AmberGlow)
                }

                Slider(
                    value = uiState.mariaState.brightnessPercent.toFloat(),
                    onValueChange = { viewModel.setBrightness(it.toInt()) },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = AmberGlow,
                        activeTrackColor = AmberGlow,
                        inactiveTrackColor = DarkBorder
                    ),
                    modifier = Modifier.testTag("slider_brightness")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = { viewModel.setBrightness(20) },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = DarkSurfaceElevated)
                    ) {
                        Text("Dim (20%)", fontSize = 11.sp, color = TextSecondary)
                    }
                    FilledTonalButton(
                        onClick = { viewModel.setBrightness(65) },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = DarkSurfaceElevated)
                    ) {
                        Text("Optimal (65%)", fontSize = 11.sp, color = TextSecondary)
                    }
                    FilledTonalButton(
                        onClick = { viewModel.setBrightness(100) },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = DarkSurfaceElevated)
                    ) {
                        Text("Max (100%)", fontSize = 11.sp, color = AmberGlow)
                    }
                }
            }
        }

        // Section: Autonomous Vision & Perception Engine
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, CrimsonBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.RemoveRedEye, contentDescription = null, tint = BloodCrimson)
                        Column {
                            Text("Screen Perception Engine", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("Visual parsing of on-screen elements", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                    Switch(
                        checked = uiState.mariaState.backgroundAssistantActive,
                        onCheckedChange = { viewModel.toggleBackgroundAssistant(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = BloodCrimson,
                            checkedTrackColor = CrimsonDeep
                        ),
                        modifier = Modifier.testTag("switch_screen_vision")
                    )
                }

                HorizontalDivider(color = DarkBorder)

                Button(
                    onClick = {
                        viewModel.triggerVibration()
                        viewModel.sendCustomVoiceQuery("Scan active screen perception")
                        Toast.makeText(context, "Scanning current device UI hierarchy...", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BloodCrimson),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Capture & Inspect Foreground Screen", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Section: Stock Tickers & Real-time Feeds (Demo chapter from video)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ShowChart, contentDescription = null, tint = TerminalEmerald)
                    Text("Stock Market & Live Telemetry", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkSurfaceElevated,
                    border = BorderStroke(1.dp, TerminalEmerald.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("VODAFONE IDEA (IDEA.NS)", fontSize = 13.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                            Text("NSE Real-time Quote Feed", fontSize = 10.sp, color = TextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("₹7.82", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TerminalEmerald)
                            Text("+3.4% Today", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TerminalEmerald)
                        }
                    }
                }

                FilledTonalButton(
                    onClick = {
                        viewModel.triggerVibration()
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/finance/quote/IDEA:NSE"))
                        context.startActivity(intent)
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = DarkSurfaceElevated)
                ) {
                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open Live Financial Chart", fontSize = 12.sp, color = TextPrimary)
                }
            }
        }

        // Section: System Settings & Permissions Bridges
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, DarkBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = NeonCyan)
                    Text("Android System Quick Toggles", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Settings.ACTION_SETTINGS)
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, DarkBorder)
                    ) {
                        Text("Settings", fontSize = 11.sp, color = TextPrimary)
                    }

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, DarkBorder)
                    ) {
                        Text("Display", fontSize = 11.sp, color = TextPrimary)
                    }

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.5f))
                    ) {
                        Text("Accessibility", fontSize = 11.sp, color = BloodCrimson)
                    }
                }
            }
        }
    }
}
