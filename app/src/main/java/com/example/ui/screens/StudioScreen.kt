package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GenerationMode
import com.example.data.model.ProgrammingLanguage
import com.example.ui.theme.*
import com.example.ui.viewmodel.CodeMindViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(
    viewModel: CodeMindViewModel,
    onNavigateToEditor: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val quickTemplates = listOf(
        "Modern Android Jetpack Compose app with Room & Coroutines",
        "High-performance FastAPI Python microservice with JWT auth",
        "Interactive HTML5 & JavaScript Canvas Particle Game",
        "Zero-cost abstraction concurrent web scraper in Rust",
        "Enterprise Spring Boot REST API with PostgreSQL in Java",
        "Optimized Red-Black Tree and Dijkstra algorithm in C++"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CodeMind AI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "Autonomous Multi-Language Coding Engine",
                    fontSize = 12.sp,
                    color = NeonCyan
                )
            }

            Surface(
                color = ElectricIndigo.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(TerminalEmerald)
                    )
                    Text(
                        text = "AUTONOMOUS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TerminalEmerald
                    )
                }
            }
        }

        // Language Selector Chips
        Text(
            text = "TARGET LANGUAGE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProgrammingLanguage.entries.forEach { lang ->
                val isSelected = uiState.selectedLanguage == lang
                val chipColor = Color(lang.iconColor)

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { viewModel.onSelectLanguage(lang) }
                        .testTag("lang_chip_${lang.id}"),
                    color = if (isSelected) chipColor.copy(alpha = 0.25f) else DarkSurfaceElevated,
                    shape = RoundedCornerShape(10.dp),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, chipColor) else null
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(chipColor)
                        )
                        Text(
                            text = lang.displayName,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) TextPrimary else TextSecondary
                        )
                    }
                }
            }
        }

        // Generation Modes
        Text(
            text = "SYNTHESIS MODE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GenerationMode.entries.forEach { mode ->
                val isSelected = uiState.selectedMode == mode

                Card(
                    modifier = Modifier
                        .width(160.dp)
                        .clickable { viewModel.onSelectMode(mode) }
                        .testTag("mode_card_${mode.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) ElectricIndigo.copy(alpha = 0.3f) else DarkSurfaceElevated
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, NeonCyan) else null
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = mode.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NeonCyan else TextPrimary
                        )
                        Text(
                            text = mode.subtitle,
                            fontSize = 10.sp,
                            color = TextSecondary,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }

        // Prompt Input Box
        Text(
            text = "DESCRIBE WHAT TO CODE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )

        OutlinedTextField(
            value = uiState.prompt,
            onValueChange = { viewModel.onPromptChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .testTag("input_code_prompt"),
            placeholder = {
                Text(
                    text = "Kuch bhi bolen (Hindi, Hinglish, English)... jaise: 'Ek complete Android app banao Room DB ke sath' ya 'FastAPI microservice with async queries'...",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurfaceElevated,
                unfocusedContainerColor = DarkSurface,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(14.dp)
        )

        // Quick Suggestion Chips
        Text(
            text = "QUICK ARCHITECTURAL TEMPLATES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickTemplates.forEach { template ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { viewModel.onPromptChange(template) },
                    color = DarkSurfaceElevated,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = template,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Generate Action Button
        Button(
            onClick = {
                viewModel.generateCode()
            },
            enabled = !uiState.isGenerating && uiState.prompt.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("button_generate_code"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ElectricIndigo,
                contentColor = Color.White,
                disabledContainerColor = DarkSurfaceElevated,
                disabledContentColor = TextMuted
            )
        ) {
            if (uiState.isGenerating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = NeonCyan,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Architecting & Coding...",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Generate",
                    tint = NeonCyan
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "GENERATE CODE NOW",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Active Solution Quick Link Card
        if (uiState.currentFiles.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToEditor() }
                    .testTag("card_latest_solution"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                border = androidx.compose.foundation.BorderStroke(1.dp, TerminalEmerald.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Ready",
                                tint = TerminalEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Code Ready (${uiState.currentFiles.size} files generated)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TerminalEmerald
                            )
                        }
                        Text(
                            text = uiState.currentFiles.firstOrNull()?.name ?: "Solution",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            maxLines = 1
                        )
                    }

                    Button(
                        onClick = onNavigateToEditor,
                        colors = ButtonDefaults.buttonColors(containerColor = TerminalEmerald),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Open IDE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkBackground)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open",
                            tint = DarkBackground,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
