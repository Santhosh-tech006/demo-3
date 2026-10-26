package com.example.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.BgPrimary
import com.example.ui.theme.BgSecondary
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBg
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.LightBlue
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun Chess3dView(
    modifier: Modifier = Modifier,
    onBookTrialClick: () -> Unit = {}
) {
    var is3dWebViewMode by remember { mutableStateOf(true) }
    var isLoadingWeb by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(BgSecondary, BgPrimary),
                    center = Offset(400f, 300f),
                    radius = 900f
                )
            )
            .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
            .testTag("chess_3d_container")
    ) {
        if (is3dWebViewMode) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.allowFileAccess = true
                        settings.allowContentAccess = true
                        settings.cacheMode = WebSettings.LOAD_DEFAULT
                        setBackgroundColor(0xFF071426.toInt())

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoadingWeb = false
                            }
                        }
                        webChromeClient = WebChromeClient()
                        loadUrl("file:///android_asset/chess3d.html")
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (isLoadingWeb) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = ElectricBlue,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Initializing 3D Chess Engine...",
                            color = LightBlue,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            // Pure Native Compose Interactive Perspective Chessboard
            NativePerspectiveChessboard(modifier = Modifier.fillMaxSize())
        }

        // Top HUD overlay: Switch between WebGL 3D & Native Canvas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = CardBg.copy(alpha = 0.85f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(ElectricBlue)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (is3dWebViewMode) "3D THREE.JS WEBGL" else "NATIVE 3D PERSPECTIVE",
                        color = LightBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                color = PrimaryBlue.copy(alpha = 0.85f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { is3dWebViewMode = !is3dWebViewMode }
                    .testTag("toggle_engine_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (is3dWebViewMode) Icons.Default.Visibility else Icons.Default.ViewInAr,
                        contentDescription = "Switch Engine",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (is3dWebViewMode) "Native 2.5D" else "WebGL 3D",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun NativePerspectiveChessboard(modifier: Modifier = Modifier) {
    var moveStep by remember { mutableIntStateOf(0) }
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    rotationAngle += dragAmount.x * 0.15f
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f + 20f

            // Perspective Grid Drawing
            val boardWidth = size.width * 0.72f
            val boardDepth = size.height * 0.55f

            // Draw board shadow
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x99000000), Color.Transparent),
                    center = Offset(cx, cy + boardDepth * 0.5f),
                    radius = boardWidth * 0.7f
                ),
                topLeft = Offset(cx - boardWidth * 0.6f, cy - 20f),
                size = androidx.compose.ui.geometry.Size(boardWidth * 1.2f, boardDepth * 1.2f)
            )

            // Draw 8x8 isometric tiles
            val tileW = boardWidth / 8f
            val tileH = boardDepth / 8f

            for (r in 0 until 8) {
                for (c in 0 until 8) {
                    val isLight = (r + c) % 2 == 0
                    val tileColor = if (isLight) Color(0xFF16345C) else Color(0xFF091B33)

                    // Skew/perspective projection
                    val topY = cy - (4 - r) * tileH * 0.85f
                    val perspectiveFactor = 0.85f + (r * 0.04f)
                    val x = cx + (c - 3.5f) * tileW * perspectiveFactor

                    drawRect(
                        color = tileColor,
                        topLeft = Offset(x - (tileW * perspectiveFactor) / 2f, topY),
                        size = androidx.compose.ui.geometry.Size(
                            tileW * perspectiveFactor * 0.92f,
                            tileH * 0.82f
                        )
                    )

                    // Draw King highlight on e4 / e5
                    if (r == 4 && c == 4) {
                        drawCircle(
                            color = ElectricBlue.copy(alpha = glowAlpha * 0.5f),
                            radius = tileW * 0.55f,
                            center = Offset(x, topY + tileH * 0.4f)
                        )
                    }
                }
            }

            // Draw glowing move path from e2 to e4
            val startY = cy - (4 - 1) * tileH * 0.85f
            val startX = cx + (4 - 3.5f) * tileW * 0.89f
            val endY = cy - (4 - 3) * tileH * 0.85f
            val endX = cx + (4 - 3.5f) * tileW * 0.97f

            drawLine(
                color = ElectricBlue,
                start = Offset(startX, startY + 10f),
                end = Offset(endX, endY + 10f),
                strokeWidth = 3f,
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        }

        // Interactive controls at bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                color = CardBg.copy(alpha = 0.9f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { moveStep = (moveStep + 1) % 4 }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Step Move",
                        tint = ElectricBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Move ${moveStep + 1}: ${getMoveName(moveStep)}", color = TextMain, fontSize = 11.sp)
                }
            }

            Surface(
                color = CardBg.copy(alpha = 0.9f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        moveStep = 0
                        rotationAngle = 0f
                    }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reset", color = TextSecondary, fontSize = 11.sp)
                }
            }
        }
    }
}

private fun getMoveName(step: Int): String {
    return when (step) {
        0 -> "1. e4"
        1 -> "1... e5"
        2 -> "2. Nf3"
        else -> "2... Nc6"
    }
}
