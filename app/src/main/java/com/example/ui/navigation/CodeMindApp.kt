package com.example.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CodeMindViewModel

@Composable
fun CodeMindApp(
    viewModel: CodeMindViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screens = listOf(
        Screen.Agent,
        Screen.Editor,
        Screen.DeviceControl,
        Screen.History,
        Screen.Settings
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                contentColor = TextPrimary,
                tonalElevation = 8.dp,
                modifier = Modifier.border(BorderStroke(1.dp, CrimsonBorder))
            ) {
                screens.forEach { screen ->
                    val isSelected = currentRoute == screen.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = BloodCrimson,
                            indicatorColor = BloodCrimson,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.route}")
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Agent.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Agent.route) {
                MariaAgentScreen(
                    viewModel = viewModel,
                    onNavigateToIDE = {
                        navController.navigate(Screen.Editor.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Screen.Editor.route) {
                EditorScreen(viewModel = viewModel)
            }
            composable(Screen.DeviceControl.route) {
                DeviceControlScreen(viewModel = viewModel)
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = viewModel,
                    onOpenProject = {
                        navController.navigate(Screen.Editor.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
