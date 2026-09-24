package com.example.ui.components

import android.annotation.SuppressLint
import android.view.View
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.*

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPreviewView(
    htmlContent: String,
    modifier: Modifier = Modifier,
    localhostUrl: String = "http://localhost:3000",
    localhostPort: Int = 3000,
    serverStatus: String = "ONLINE (200 OK)",
    isBackendServer: Boolean = false
) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var isMobileViewport by remember { mutableStateOf(false) }
    var isRendererCrashed by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            try {
                webViewInstance?.stopLoading()
                webViewInstance?.destroy()
                webViewInstance = null
            } catch (_: Exception) {}
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        border = BorderStroke(1.dp, CrimsonBorder.copy(alpha = 0.8f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chrome Localhost Browser Address Bar
            Surface(
                color = DarkSurface,
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Window control dots
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(CoralRed))
                            Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(AmberGlow))
                            Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(TerminalEmerald))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBackendServer) "API LOCALHOST HOST" else "WEB LOCALHOST PREVIEW",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isBackendServer) NeonCyan else BloodCrimson,
                                letterSpacing = 0.5.sp
                            )
                        }

                        // Localhost Online Status Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TerminalEmerald.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, TerminalEmerald.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(TerminalEmerald)
                                )
                                Text(
                                    text = ":$localhostPort $serverStatus",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TerminalEmerald
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Browser Address Input Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkBackground)
                            .border(BorderStroke(1.dp, DarkBorder), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Localhost Secure",
                                tint = TerminalEmerald,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = localhostUrl,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary,
                                maxLines = 1
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            // Viewport switcher
                            IconButton(
                                onClick = { isMobileViewport = !isMobileViewport },
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = if (isMobileViewport) Icons.Default.Tv else Icons.Default.Smartphone,
                                    contentDescription = "Toggle Viewport",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Reload button
                            IconButton(
                                onClick = {
                                    isRendererCrashed = false
                                    webViewInstance?.loadDataWithBaseURL("http://localhost:$localhostPort/", htmlContent, "text/html", "UTF-8", null)
                                },
                                modifier = Modifier
                                    .size(26.dp)
                                    .testTag("btn_reload_localhost")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reload Web View",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // WebView Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(Color(0xFF09090D)),
                contentAlignment = Alignment.TopCenter
            ) {
                if (isRendererCrashed) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = TerminalEmerald,
                                modifier = Modifier.size(42.dp)
                            )
                            Text(
                                text = "Preview Process Isolated (Safe)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "The WebView engine was safely isolated without terminating the app. Tap below to re-render in software mode.",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.widthIn(max = 280.dp)
                            )
                            Button(
                                onClick = { isRendererCrashed = false },
                                colors = ButtonDefaults.buttonColors(containerColor = BloodCrimson),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reload Preview", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .then(if (isMobileViewport) Modifier.width(320.dp) else Modifier.fillMaxWidth())
                            .fillMaxHeight()
                    ) {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    try {
                                        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                                    } catch (_: Exception) {}

                                    settings.apply {
                                        javaScriptEnabled = true
                                        domStorageEnabled = true
                                        loadWithOverviewMode = true
                                        useWideViewPort = true
                                        databaseEnabled = false
                                        allowFileAccess = false
                                        allowContentAccess = false
                                        cacheMode = WebSettings.LOAD_NO_CACHE
                                    }
                                    webChromeClient = WebChromeClient()
                                    webViewClient = object : WebViewClient() {
                                        override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                                            isRendererCrashed = true
                                            try {
                                                view?.destroy()
                                            } catch (_: Exception) {}
                                            return true // CRITICAL: Tells OS the host handled exit and prevents app termination
                                        }
                                    }
                                    tag = htmlContent
                                    loadDataWithBaseURL("http://localhost:$localhostPort/", htmlContent, "text/html", "UTF-8", null)
                                    webViewInstance = this
                                }
                            },
                            update = { view ->
                                if (view.tag != htmlContent) {
                                    view.tag = htmlContent
                                    view.loadDataWithBaseURL("http://localhost:$localhostPort/", htmlContent, "text/html", "UTF-8", null)
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
