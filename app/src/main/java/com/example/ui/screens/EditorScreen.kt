package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProgrammingLanguage
import com.example.ui.components.AkExploitsEmblem
import com.example.ui.components.CodeEditorView
import com.example.ui.components.EmblemSize
import com.example.ui.components.FileTabsBar
import com.example.ui.components.TerminalOutputView
import com.example.ui.components.WebPreviewView
import com.example.ui.theme.*
import com.example.ui.viewmodel.CodeMindViewModel

enum class EditorPane(val title: String, val icon: ImageVector) {
    EDITOR("Code", Icons.Default.Code),
    LOCALHOST("Localhost Live", Icons.Default.Language),
    TERMINAL("Terminal", Icons.Default.Terminal),
    NOTES("Architecture", Icons.Default.Info)
}

@Composable
fun EditorScreen(
    viewModel: CodeMindViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var activePane by remember { mutableStateOf(EditorPane.EDITOR) }
    var showLanguageDropdown by remember { mutableStateOf(false) }

    val activeFile = uiState.currentFiles.getOrNull(uiState.activeFileIndex)
    val result = uiState.executionResult

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Multi-file tabs if project has multiple files
        if (uiState.currentFiles.size > 1) {
            FileTabsBar(
                files = uiState.currentFiles,
                selectedIndex = uiState.activeFileIndex,
                onSelectFile = { viewModel.onSelectFile(it) }
            )
        }

        // Top Toolstrip / Actions Bar
        Surface(
            color = DarkSurface,
            border = BorderStroke(1.dp, CrimsonBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: AK EXPLOITS Mini Emblem & Active File
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AkExploitsEmblem(
                        size = EmblemSize.MINI,
                        onClick = {
                            viewModel.triggerVibration()
                            Toast.makeText(context, "AK EXPLOITS • Bypass IDE Active", Toast.LENGTH_SHORT).show()
                        }
                    )

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = activeFile?.name ?: "No file loaded",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                maxLines = 1
                            )
                            Surface(
                                color = BloodCrimson.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "BYPASS IDE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = BloodCrimson,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }

                        // Language Selector Dropdown trigger
                        Box {
                            val activeLang = uiState.selectedLanguage
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable { showLanguageDropdown = true }
                                    .padding(vertical = 1.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(activeLang.iconColor))
                                )
                                Text(
                                    text = "${activeLang.displayName} (:${activeLang.defaultPort}) ▾",
                                    fontSize = 11.sp,
                                    color = NeonCyan,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            DropdownMenu(
                                expanded = showLanguageDropdown,
                                onDismissRequest = { showLanguageDropdown = false },
                                modifier = Modifier
                                    .background(DarkSurfaceElevated)
                                    .heightIn(max = 340.dp)
                            ) {
                                Text(
                                    text = "SELECT LANGUAGE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextMuted,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                                HorizontalDivider(color = DarkBorder)
                                ProgrammingLanguage.entries.forEach { lang ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(lang.iconColor))
                                                )
                                                Text(
                                                    text = lang.displayName,
                                                    fontSize = 12.sp,
                                                    fontWeight = if (uiState.selectedLanguage == lang) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (uiState.selectedLanguage == lang) NeonCyan else TextPrimary
                                                )
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = ":${lang.defaultPort}",
                                                    fontSize = 10.sp,
                                                    color = TextMuted
                                                )
                                            }
                                        },
                                        onClick = {
                                            viewModel.onSelectLanguage(lang)
                                            showLanguageDropdown = false
                                            Toast.makeText(context, "Language switched to ${lang.displayName}", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Right: Action Buttons (Run, Fix, Save to Vault)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Save to Vault
                    IconButton(
                        onClick = {
                            viewModel.triggerVibration()
                            viewModel.saveCurrentProjectToVault()
                            Toast.makeText(context, "Code saved in AK EXPLOITS Vault!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("btn_save_vault")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Save to Vault",
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Autonomous Code Doctor
                    IconButton(
                        onClick = {
                            viewModel.triggerVibration()
                            activePane = EditorPane.EDITOR
                            viewModel.performSmartCodeDoctor("Auto repair requested from IDE")
                            Toast.makeText(context, "Maria Code Doctor: Analyzing & Repairing...", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("btn_code_doctor")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoFixHigh,
                            contentDescription = "Code Problem Doctor",
                            tint = AmberGlow,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Run / Localhost Button
                    Button(
                        onClick = {
                            viewModel.triggerVibration()
                            activePane = if (uiState.selectedLanguage == ProgrammingLanguage.HTML_WEB) EditorPane.LOCALHOST else EditorPane.TERMINAL
                            viewModel.runCode()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BloodCrimson),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_run_code")
                    ) {
                        if (uiState.isRunningCode) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(12.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Running...", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Run",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("RUN", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Workspace Pane Switcher Bar (Segmented Tabs)
        Surface(
            color = DarkSurfaceElevated,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EditorPane.entries.forEach { pane ->
                    val isSelected = activePane == pane
                    FilterChip(
                        selected = isSelected,
                        onClick = { activePane = pane },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(pane.title, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                if (pane == EditorPane.LOCALHOST && result != null) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(TerminalEmerald)
                                    )
                                }
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = pane.icon,
                                contentDescription = pane.title,
                                modifier = Modifier.size(14.dp),
                                tint = if (isSelected) BloodCrimson else TextSecondary
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BloodCrimson.copy(alpha = 0.2f),
                            selectedLabelColor = TextPrimary,
                            containerColor = DarkSurface,
                            labelColor = TextSecondary
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) BloodCrimson else DarkBorder
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }

        // Active Pane Viewport (Occupies remaining height completely with NO nested scroll conflicts!)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
        ) {
            when (activePane) {
                EditorPane.EDITOR -> {
                    if (activeFile != null) {
                        CodeEditorView(
                            code = activeFile.content,
                            language = activeFile.language,
                            modifier = Modifier.fillMaxSize(),
                            onShare = { viewModel.shareActiveFile(context) }
                        )
                    } else {
                        EmptyWorkspaceCard(
                            title = "No Code File Loaded",
                            subtitle = "Ask Maria to generate or fix code, or load a project from Vault.",
                            actionLabel = "Open Vault",
                            onAction = { viewModel.saveCurrentProjectToVault() }
                        )
                    }
                }

                EditorPane.LOCALHOST -> {
                    if (result != null && result.webHtml.isNotBlank()) {
                        val currentLang = uiState.selectedLanguage
                        WebPreviewView(
                            htmlContent = result.webHtml,
                            localhostUrl = "http://localhost:${currentLang.defaultPort}/",
                            localhostPort = currentLang.defaultPort,
                            serverStatus = "200 OK • ONLINE",
                            isBackendServer = !result.isWebPreview,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        EmptyWorkspaceCard(
                            title = "Localhost Server Standby",
                            subtitle = "Tap 'RUN' above to start the simulated localhost server and view the live app preview.",
                            actionLabel = "▶ Start Localhost Server",
                            onAction = {
                                viewModel.triggerVibration()
                                viewModel.runCode()
                            }
                        )
                    }
                }

                EditorPane.TERMINAL -> {
                    if (result != null) {
                        TerminalOutputView(
                            result = result,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        EmptyWorkspaceCard(
                            title = "Terminal Console Idle",
                            subtitle = "Standard output and execution diagnostics will stream here when code is compiled or executed.",
                            actionLabel = "▶ Execute Code",
                            onAction = {
                                viewModel.triggerVibration()
                                viewModel.runCode()
                            }
                        )
                    }
                }

                EditorPane.NOTES -> {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        border = BorderStroke(1.dp, CrimsonBorder.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = BloodCrimson)
                                Text(
                                    text = "Architecture & Execution Notes",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            HorizontalDivider(color = DarkBorder)

                            Text(
                                text = "Project: ${uiState.prompt.ifBlank { "Autonomous Solution" }}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )

                            Text(
                                text = "Language: ${uiState.selectedLanguage.displayName} (Localhost Port ${uiState.selectedLanguage.defaultPort})",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )

                            Text(
                                text = "Solution Breakdown:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Text(
                                text = uiState.explanation.ifBlank { "Autonomous code synthesis generated by Maria AI for AK EXPLOITS." },
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = TextSecondary
                            )

                            if (result?.stderr?.isNotBlank() == true) {
                                Surface(
                                    color = CoralRed.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, CoralRed)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Compiler Diagnostic Warning:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CoralRed)
                                        Text(result.stderr, fontSize = 11.sp, color = TextPrimary, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyWorkspaceCard(
    title: String,
    subtitle: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = null,
                    tint = BloodCrimson.copy(alpha = 0.6f),
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.widthIn(max = 280.dp)
                )
                Button(
                    onClick = onAction,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BloodCrimson)
                ) {
                    Text(actionLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
