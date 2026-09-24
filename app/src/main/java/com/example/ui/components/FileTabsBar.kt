package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CodeFile
import com.example.ui.theme.*

@Composable
fun FileTabsBar(
    files: List<CodeFile>,
    selectedIndex: Int,
    onSelectFile: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (files.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkSurfaceElevated)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        files.forEachIndexed { index, file ->
            val isSelected = index == selectedIndex
            val chipBg = if (isSelected) DarkBackground else Color.Transparent
            val borderModifier = if (isSelected) {
                Modifier.background(DarkBackground, RoundedCornerShape(8.dp))
            } else {
                Modifier
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .then(borderModifier)
                    .clickable { onSelectFile(index) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("tab_file_$index"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val icon = when {
                    file.name.endsWith(".html") || file.name.endsWith(".css") || file.name.endsWith(".js") -> Icons.Default.Web
                    file.name.endsWith(".kt") || file.name.endsWith(".py") || file.name.endsWith(".rs") -> Icons.Default.Code
                    else -> Icons.Default.Description
                }

                Icon(
                    imageVector = icon,
                    contentDescription = file.name,
                    tint = if (isSelected) NeonCyan else TextMuted,
                    modifier = Modifier.size(16.dp)
                )

                Text(
                    text = file.name,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) TextPrimary else TextSecondary
                )
            }
        }
    }
}
