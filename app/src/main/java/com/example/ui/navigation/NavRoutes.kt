package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Agent : Screen("agent", "Maria Agent", Icons.Default.SmartToy)
    data object Editor : Screen("editor", "Bypass IDE", Icons.Default.Code)
    data object DeviceControl : Screen("device", "Device Control", Icons.Default.PhoneAndroid)
    data object History : Screen("history", "Projects", Icons.Default.FolderSpecial)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}
