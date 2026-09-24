package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExecutionResult
import com.example.ui.theme.*

@Composable
fun TerminalOutputView(
    result: ExecutionResult,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = EditorCodeBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(CoralRed))
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(AmberGlow))
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(TerminalEmerald))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TERMINAL CONSOLE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Terminal Logs", result.stdout))
                            Toast.makeText(context, "Terminal output copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(28.dp).testTag("btn_copy_terminal")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy logs",
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (result.exitCode == 0) TerminalEmerald.copy(alpha = 0.2f) else CoralRed.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = if (result.exitCode == 0) "EXIT 0 (OK)" else "EXIT ${result.exitCode} (ERR)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (result.exitCode == 0) TerminalEmerald else CoralRed,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Output body
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
                    .horizontalScroll(rememberScrollState())
            ) {
                Text(
                    text = result.stdout.ifBlank { "Execution finished with no standard output." },
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = if (result.exitCode == 0) TerminalEmerald else CoralRed
                )
            }
        }
    }
}
