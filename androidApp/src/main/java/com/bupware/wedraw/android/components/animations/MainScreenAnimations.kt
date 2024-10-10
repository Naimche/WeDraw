package com.bupware.wedraw.android.components.animations

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChipPop(content: @Composable () -> Unit, show: Boolean){

    AnimatedVisibility(
        visible = show,
        enter = scaleIn(animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.5f)) + expandVertically(expandFrom = Alignment.CenterVertically)  ,
        exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
    ) {
        content()
    }

}
