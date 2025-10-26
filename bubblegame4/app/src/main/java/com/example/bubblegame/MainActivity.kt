package com.example.bubblegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbleGameScreen()
        }
    }
}

data class Bubble(
    val id: Int,
    val position: Offset,
    val radius: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color
)

data class GameState(
    val bubbles: List<Bubble>,
    val score: Int,
    val isGameOver: Boolean
)

@Composable
fun BubbleGameScreen() {
    var gameState by remember { mutableStateOf(createInitialGameState()) }

    // 버블 위치 업데이트 루프
    LaunchedEffect(Unit) {
        while (true) { // 게임 종료와 상관없이 계속 실행
            delay(16L) // 약 60fps
            if (!gameState.isGameOver) {
                gameState = gameState.copy(bubbles = updateBubbles(gameState.bubbles))
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            // 버블들 그리기
            gameState.bubbles.forEach { bubble ->
                BubbleComposable(
                    bubble = bubble,
                    onClick = {
                        gameState = handleBubbleClick(gameState, bubble.id)
                    }
                )
            }

            // 점수 표시
            Text(
                text = "Score: ${gameState.score}",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            )

            // 게임 종료 시 메시지
            if (gameState.isGameOver) {
                Text(
                    text = "🎉 Game Over!",
                    fontSize = 32.sp,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Composable
fun BubbleComposable(bubble: Bubble, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .offset(x = bubble.position.x.dp, y = bubble.position.y.dp)
            .size((bubble.radius * 2).dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(color = bubble.color)
        }
    }
}

fun createInitialGameState(): GameState {
    val bubbles = List(5) { i ->
        Bubble(
            id = i,
            position = Offset(
                Random.nextFloat() * 300f,
                Random.nextFloat() * 600f
            ),
            radius = Random.nextFloat() * 30f + 20f,
            velocityX = Random.nextFloat() * 4f - 2f,
            velocityY = Random.nextFloat() * 4f - 2f,
            color = Color(
                red = Random.nextFloat(),
                green = Random.nextFloat(),
                blue = Random.nextFloat()
            )
        )
    }
    return GameState(bubbles = bubbles, score = 0, isGameOver = false)
}

fun updateBubbles(bubbles: List<Bubble>): List<Bubble> {
    return bubbles.map { bubble ->
        val newX = bubble.position.x + bubble.velocityX
        val newY = bubble.position.y + bubble.velocityY
        bubble.copy(position = Offset(newX, newY))
    }
}

fun handleBubbleClick(gameState: GameState, id: Int): GameState {
    val newBubbles = gameState.bubbles.filterNot { it.id == id }
    val newScore = gameState.score + 1
    val gameOver = newBubbles.isEmpty()
    return GameState(bubbles = newBubbles, score = newScore, isGameOver = gameOver)
}

@Preview(showBackground = true)
@Composable
fun PreviewBubbleGame() {
    BubbleGameScreen()
}
