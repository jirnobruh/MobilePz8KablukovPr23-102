/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.pr8kablukovpr23102.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.TimeText
import kotlinx.coroutines.launch
import com.example.pr8kablukovpr23102.R
import com.example.pr8kablukovpr23102.presentation.theme.Pr8KablukovPr23102Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            AppWithPager()
        }
    }
}

enum class Screen { NONE, CLICKER, STOPWATCH, CASES }

@Composable
fun AppWithPager() {
    // Pager только для Start и Menu (индексы 0 и 1)
    val pageCount = 2
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }
    val scope = rememberCoroutineScope()

    // Текущее открытое функциональное окно; NONE = ничего не открыто (показываем pager)
    var currentScreen by remember { mutableStateOf(Screen.NONE) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Время вверху
        TimeText(modifier = Modifier.align(Alignment.TopEnd).padding(8.dp))

        // Если currentScreen == NONE — показываем Pager (Start + Menu)
        if (currentScreen == Screen.NONE) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> StartScreen(
                        onSwipeToMenu = { scope.launch { pagerState.animateScrollToPage(1) } }
                    )
                    1 -> MenuScreen(
                        onOpenClicker = { currentScreen = Screen.CLICKER },
                        onOpenStopwatch = { currentScreen = Screen.STOPWATCH },
                        onOpenCases = { currentScreen = Screen.CASES }
                    )
                }
            }
        } else {
            // Показываем выбранный экран поверх (с кнопкой Назад)
            when (currentScreen) {
                Screen.CLICKER -> ClickerScreen(onBack = { currentScreen = Screen.NONE })
                Screen.STOPWATCH -> StopwatchScreen(onBack = { currentScreen = Screen.NONE })
                Screen.CASES -> CasesScreen(onBack = { currentScreen = Screen.NONE })
                else -> {}
            }
        }
    }
}


@Composable
fun StartScreen(onSwipeToMenu: () -> Unit) {
    // Простой стартовый экран — иконка и подпись (можно заменить на ваш StartScreen)
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        // Время в правом верхнем углу
        TimeText(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp)
        )

        // Центр: иконка и подпись
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ограничиваем размер картинки: можно менять 72.dp на любое значение
            Image(
                painter = painterResource(id = R.drawable.icon_police),
                contentDescription = "Police icon",
                modifier = Modifier
                    .size(72.dp)           // фиксированный размер
                    .clip(CircleShape),   // опционально: скруглить
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "WSK Police", // "WSK Police"
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun MenuScreen(
    onOpenClicker: () -> Unit,
    onOpenStopwatch: () -> Unit,
    onOpenCases: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Сделал чуть меньше: высота 36.dp, ширина 80% экрана
            Button(
                onClick = onOpenClicker,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(36.dp)
            ) { Text(text = "Кликер") }

            Button(
                onClick = onOpenStopwatch,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(36.dp)
            ) { Text(text = "Секундомер") }

            Button(
                onClick = onOpenCases,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(36.dp)
            ) { Text(text = "Дела") }
        }
    }
}


@Composable
fun ClickerScreen(onBack: () -> Unit) {
    var count by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Кнопка Назад сверху слева
        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(6.dp)
                .height(28.dp)
        ) {
            Text(text = "Назад")
        }

        // Основной контент по центру
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Счёт: $count")
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { if (count > 0) count-- }) { Text("-") }
                Button(onClick = { count++ }) { Text("+") }
            }
        }
    }
}


@Composable
fun StopwatchScreen(onBack: () -> Unit) {
    // Упрощённый UI секундомера — реализацию таймера можно добавить отдельно
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(6.dp)
                .height(28.dp)
        ) {
            Text(text = "Назад")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "00:00:00")
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* Start/Pause */ }) { Text("Start") }
                Button(onClick = { /* Reset with confirmation */ }) { Text("Reset") }
            }
        }
    }
}

@Composable
fun CasesScreen(onBack: () -> Unit) {
    // Экран с количеством открытых дел
    val openCases = remember { mutableStateOf(5) }
    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onBack, modifier = Modifier.align(Alignment.TopStart).padding(6.dp).height(28.dp)) {
            Text("Назад")
        }
        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Открытых дел: ${openCases.value}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* закрыть дело с подтверждением */ }) { Text("Закрыть дело") }
        }
    }

}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    AppWithPager()
}