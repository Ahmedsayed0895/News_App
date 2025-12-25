package com.task.newsapp.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.task.newsapp.R

@Composable
fun LottieAnimated(type: LottieType) {
    var isPlaying by remember { mutableStateOf(true) }
    var speed by remember { mutableFloatStateOf(1f) }
    val resource = when (type) {
        LottieType.ERROR -> R.raw.error
        LottieType.LOADING -> R.raw.searching
        LottieType.REFRESH -> R.raw.refresh
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resource))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(100.dp)
    )
}

enum class LottieType {
    ERROR,
    LOADING,
    REFRESH
}