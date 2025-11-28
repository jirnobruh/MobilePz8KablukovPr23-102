/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.pr8kablukovpr23102.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.wear.compose.material.Button
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.asPaddingValues
import com.example.pr8kablukovpr23102.R
import kotlinx.coroutines.launch

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
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(safePadding) // всё внутри видимой области
    ) {
        // Центральный контент: кнопка назад + счёт + кнопки управления
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Маленькая компактная кнопка назад, часть центрального интерфейса
            Button(
                onClick = onBack,
                modifier = Modifier
                    .size(36.dp) // компактный круглый размер
            ) {
                Text("←")
            }

            Spacer(modifier = Modifier.height(6.dp))

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
    // safe area для круглого экрана
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()

    // состояние секундомера
    var elapsedMillis by remember { mutableStateOf(0L) }      // текущее прошедшее время
    var isRunning by remember { mutableStateOf(false) }       // идёт ли таймер
    var startBase by remember { mutableStateOf(0L) }         // базовое время SystemClock при старте
    var showControls by remember { mutableStateOf(false) }   // показывать ли кнопки

    // Логика обновления времени, пока isRunning == true
    LaunchedEffect(isRunning) {
        if (isRunning) {
            // при старте корректируем базу так, чтобы учитывать уже накопленное elapsedMillis
            startBase = SystemClock.elapsedRealtime() - elapsedMillis
            while (isRunning) {
                elapsedMillis = SystemClock.elapsedRealtime() - startBase
                delay(100L) // обновляем каждые 100 мс
            }
        }
    }

    // Форматирование в HH:MM:SS
    fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(safePadding), // гарантируем видимую область на круглых часах
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(4.dp)
        ) {
            // Маленькая кнопка назад вверху центрального блока (если нужна)
            Button(
                onClick = onBack,
                modifier = Modifier
                    .size(36.dp)
            ) {
                Text("←")
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Таймер: кликабельный — по клику показываем/скрываем контролы
            Text(
                text = formatTime(elapsedMillis),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable { showControls = !showControls }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Контролы: показываются только при showControls == true
            if (showControls) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Start
                    Button(
                        onClick = {
                            if (!isRunning) {
                                // старт: isRunning -> true, LaunchedEffect установит startBase
                                isRunning = true
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp) // скруглённые прямоугольники
                    ) {
                        Text("Start")
                    }

                    // Pause
                    Button(
                        onClick = {
                            if (isRunning) {
                                // пауза: останавливаем обновление; elapsedMillis уже актуален
                                isRunning = false
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Pause")
                    }

                    // Reset
                    Button(
                        onClick = {
                            isRunning = false
                            elapsedMillis = 0L
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}

@Composable
fun CasesScreen(onBack: () -> Unit) {
    val openCases = remember { mutableStateOf(5) }
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(safePadding)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.size(36.dp)
            ) {
                Text("←")
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Открытых дел: ${openCases.value}")
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    AppWithPager()
}