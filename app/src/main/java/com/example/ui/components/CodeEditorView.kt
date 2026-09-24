package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CodeEditorView(
    code: String,
    language: String,
    modifier: Modifier = Modifier,
    onShare: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val highlightedCode = remember(code, language) {
        SyntaxHighlighter.highlight(code, language)
    }

    val lines = remember(code) { code.lines() }
    val lineCount = lines.size
    val charCount = code.length

    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = EditorCodeBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Action bar on top of the code
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = ElectricIndigo.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = language.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Text(
                        text = "$lineCount lines • $charCount chars",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Code", code))
                            Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(36.dp).testTag("button_copy_code")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy code",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    if (onShare != null) {
                        IconButton(
                            onClick = onShare,
                            modifier = Modifier.size(36.dp).testTag("button_share_code")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share code",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Gutter + Code Editor container
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(verticalScroll)
            ) {
                // Line numbers gutter
                Column(
                    modifier = Modifier
                        .background(EditorGutterBg)
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    for (i in 1..lineCount) {
                        Text(
                            text = "$i",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = TextMuted.copy(alpha = 0.6f)
                        )
                    }
                }

                // Code content with horizontal scroll
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
                        .horizontalScroll(horizontalScroll)
                ) {
                    Text(
                        text = highlightedCode,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}
