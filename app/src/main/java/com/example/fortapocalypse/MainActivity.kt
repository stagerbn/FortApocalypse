package com.example.fortapocalypse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FortApocalypseGame()
                }
            }
        }
    }
}

data class Hostage(
    var position: Offset,
    var rescued: Boolean = false
)

@Composable
fun FortApocalypseGame() {
    var helicopterX by remember { mutableFloatStateOf(220f) }
    var helicopterY by remember { mutableFloatStateOf(280f) }
    var velocityX by remember { mutableFloatStateOf(0f) }
    var velocityY by remember { mutableFloatStateOf(0f) }
    var fuel by remember { mutableFloatStateOf(100f) }
    var score by remember { mutableIntStateOf(0) }
    var level by remember { mutableIntStateOf(1) }
    var gameOver by remember { mutableStateOf(false) }
    var canvasWidth by remember { mutableFloatStateOf(1f) }
    var canvasHeight by remember { mutableFloatStateOf(1f) }
    val hostages = remember { mutableStateListOf<Hostage>() }

    fun resetLevel(newLevel: Int = 1) {
        hostages.clear()
        repeat(3 + newLevel) {
            hostages += Hostage(
                position = Offset(
                    x = Random.nextFloat() * (canvasWidth - 80f).coerceAtLeast(80f) + 40f,
                    y = Random.nextFloat() * (canvasHeight - 120f).coerceAtLeast(120f) + 60f
                )
            )
        }
        helicopterX = 220f
        helicopterY = 280f
        velocityX = 0f
        velocityY = 0f
        fuel = 100f
        level = newLevel
        gameOver = false
    }

    LaunchedEffect(canvasWidth, canvasHeight) {
        if (canvasWidth > 1f && canvasHeight > 1f && hostages.isEmpty()) {
            resetLevel()
        }
    }

    LaunchedEffect(gameOver, level) {
        while (!gameOver) {
            delay(16)
            velocityX *= 0.94f
            velocityY *= 0.94f
            helicopterX = (helicopterX + velocityX).coerceIn(30f, canvasWidth - 30f)
            helicopterY = (helicopterY + velocityY).coerceIn(30f, canvasHeight - 30f)
            fuel = (fuel - 0.04f * level).coerceAtLeast(0f)

            hostages.forEach { hostage ->
                if (!hostage.rescued && abs(helicopterX - hostage.position.x) < 34f && abs(helicopterY - hostage.position.y) < 34f) {
                    hostage.rescued = true
                    score += 100
                    fuel = (fuel + 8f).coerceAtMost(100f)
                }
            }

            val rescuedCount = hostages.count { it.rescued }
            if (rescuedCount == hostages.size && hostages.isNotEmpty()) {
                score += 300
                resetLevel(level + 1)
            }

            if (fuel <= 0f) {
                gameOver = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101820))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Score: $score", color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
            Text("Level: $level", color = Color(0xFFFFD54F), fontWeight = FontWeight.Bold)
            Text("Fuel: ${fuel.toInt()}%", color = if (fuel < 20f) Color.Red else Color(0xFF76FF03), fontWeight = FontWeight.Bold)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF0A0F14), RoundedCornerShape(14.dp))
                .pointerInput(gameOver) {
                    detectDragGestures { _, dragAmount ->
                        if (!gameOver) {
                            velocityX += dragAmount.x * 0.05f
                            velocityY += dragAmount.y * 0.05f
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                canvasWidth = size.width
                canvasHeight = size.height

                drawRect(Color(0xFF132A3A))

                for (i in 0..18) {
                    drawCircle(
                        color = Color(0xFF1D3B50),
                        radius = 14f + (i % 5) * 3,
                        center = Offset((i * 83f) % size.width, ((i * 129f) + level * 37f) % size.height)
                    )
                }

                hostages.filter { !it.rescued }.forEach {
                    drawCircle(Color(0xFFFFF176), radius = 11f, center = it.position)
                    drawLine(
                        Color(0xFFFFF59D),
                        start = it.position + Offset(0f, -12f),
                        end = it.position + Offset(0f, -28f),
                        strokeWidth = 3f
                    )
                }

                drawCircle(
                    color = Color(0xFF4DD0E1),
                    radius = 16f,
                    center = Offset(helicopterX, helicopterY)
                )
                drawRect(
                    color = Color(0xFF80DEEA),
                    topLeft = Offset(helicopterX - 24f, helicopterY - 4f),
                    size = androidx.compose.ui.geometry.Size(48f, 8f)
                )

                drawLine(
                    color = Color(0xFFCFD8DC),
                    start = Offset(helicopterX - 12f, helicopterY + 18f),
                    end = Offset(helicopterX + 12f, helicopterY + 18f),
                    strokeWidth = 4f
                )
            }

            if (gameOver) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color(0xCC000000), RoundedCornerShape(16.dp))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Mission gescheitert", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Text("Final Score: $score", color = Color(0xFF00E5FF))
                    Button(onClick = {
                        score = 0
                        resetLevel(1)
                    }) {
                        Text("Neustart")
                    }
                }
            }
        }

        Text(
            text = "Steuerung: Im Spielfeld ziehen, um den Helikopter zu beschleunigen. Sammle alle Überlebenden und spare Treibstoff!",
            color = Color(0xFFB0BEC5),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
        )
    }
}
