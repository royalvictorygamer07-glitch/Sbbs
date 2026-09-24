package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CodeProjectEntity
import com.example.data.model.ProgrammingLanguage
import com.example.ui.components.AkExploitsEmblem
import com.example.ui.components.EmblemSize
import com.example.ui.theme.*
import com.example.ui.viewmodel.CodeMindViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: CodeMindViewModel,
    onOpenProject: () -> Unit,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.savedProjects.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var filterFavoritesOnly by remember { mutableStateOf(false) }
    var selectedLanguageFilter by remember { mutableStateOf<String?>(null) }

    val filteredProjects = remember(projects, searchQuery, filterFavoritesOnly, selectedLanguageFilter) {
        projects.filter { project ->
            val matchesQuery = searchQuery.isBlank() ||
                    project.title.contains(searchQuery, ignoreCase = true) ||
                    project.prompt.contains(searchQuery, ignoreCase = true) ||
                    project.language.contains(searchQuery, ignoreCase = true)
            val matchesFav = !filterFavoritesOnly || project.isFavorite
            val matchesLang = selectedLanguageFilter == null || project.language.equals(selectedLanguageFilter, ignoreCase = true)
            matchesQuery && matchesFav && matchesLang
        }
    }

    val dateFormatter = remember { SimpleDateFormat("MMM d, yyyy • HH:mm", Locale.getDefault()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Vault Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
            border = BorderStroke(1.dp, CrimsonBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AkExploitsEmblem(size = EmblemSize.MEDIUM)
                    Column {
                        Text(
                            text = "AK EXPLOITS CODE VAULT",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "${projects.size} Projects Saved • Multi-Language Localhost Ready",
                            fontSize = 11.sp,
                            color = NeonCyan
                        )
                    }
                }

                FilterChip(
                    selected = filterFavoritesOnly,
                    onClick = { filterFavoritesOnly = !filterFavoritesOnly },
                    label = { Text("Starred", fontSize = 10.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = if (filterFavoritesOnly) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Filter Favorites",
                            tint = AmberGlow,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AmberGlow.copy(alpha = 0.2f),
                        selectedLabelColor = AmberGlow,
                        containerColor = DarkSurfaceCard,
                        labelColor = TextSecondary
                    )
                )
            }
        }

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_search_projects"),
            placeholder = { Text("Search code vault by title, language, or prompt...", fontSize = 13.sp, color = TextMuted) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurfaceElevated,
                unfocusedContainerColor = DarkSurface,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // Language Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedLanguageFilter == null,
                onClick = { selectedLanguageFilter = null },
                label = { Text("All (${projects.size})", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BloodCrimson.copy(alpha = 0.3f),
                    selectedLabelColor = TextPrimary,
                    containerColor = DarkSurfaceCard,
                    labelColor = TextSecondary
                )
            )

            listOf("html", "python", "kotlin", "javascript", "rust", "cpp", "java", "php", "go", "sql").forEach { langId ->
                val count = projects.count { it.language.equals(langId, ignoreCase = true) }
                if (count > 0 || projects.isEmpty()) {
                    val lang = ProgrammingLanguage.fromId(langId)
                    FilterChip(
                        selected = selectedLanguageFilter == langId,
                        onClick = {
                            selectedLanguageFilter = if (selectedLanguageFilter == langId) null else langId
                        },
                        label = { Text("${lang.displayName} ($count)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(lang.iconColor).copy(alpha = 0.3f),
                            selectedLabelColor = TextPrimary,
                            containerColor = DarkSurfaceCard,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }

        // List of projects
        if (filteredProjects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderZip,
                        contentDescription = "No projects",
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = if (searchQuery.isNotBlank()) "No matching solutions found." else "Vault is currently empty.",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Synthesize code in the Studio or Agent tab to auto-save solutions here.",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredProjects, key = { it.id }) { item ->
                    ProjectCard(
                        project = item,
                        dateStr = dateFormatter.format(Date(item.createdAt)),
                        onOpen = {
                            viewModel.loadProject(item)
                            onOpenProject()
                        },
                        onRunLocalhost = {
                            viewModel.runLocalhostProject(item)
                            onOpenProject()
                        },
                        onToggleFavorite = {
                            viewModel.toggleFavorite(item.id, item.isFavorite)
                        },
                        onDelete = {
                            viewModel.deleteProject(item.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectCard(
    project: CodeProjectEntity,
    dateStr: String,
    onOpen: () -> Unit,
    onRunLocalhost: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val lang = remember(project.language) { ProgrammingLanguage.fromId(project.language) }
    val langColor = Color(lang.iconColor)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .testTag("card_project_${project.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(langColor))
                    Text(
                        text = lang.displayName.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = langColor
                    )
                    Text(
                        text = "•",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Text(
                        text = ":${lang.defaultPort} Localhost",
                        fontSize = 10.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "•",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Text(
                        text = dateStr,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (project.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favorite",
                            tint = if (project.isFavorite) AmberGlow else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Text(
                text = project.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )

            Text(
                text = project.prompt,
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 2,
                lineHeight = 16.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Direct Localhost Run Action
                FilledTonalButton(
                    onClick = onRunLocalhost,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = BloodCrimson.copy(alpha = 0.25f),
                        contentColor = TextPrimary
                    ),
                    border = BorderStroke(1.dp, BloodCrimson.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("btn_run_localhost_${project.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Run Localhost",
                        modifier = Modifier.size(14.dp),
                        tint = BloodCrimson
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("▶ Run on Localhost", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onOpen,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceCard),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, DarkBorder),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Open Code", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Load",
                        modifier = Modifier.size(12.dp),
                        tint = TextSecondary
                    )
                }
            }
        }
    }
}
