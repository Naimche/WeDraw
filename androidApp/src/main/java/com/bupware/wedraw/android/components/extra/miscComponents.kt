package com.bupware.wedraw.android.components.extra

import androidx.activity.compose.BackHandler
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString

@Composable
fun BackHandler(onBackPressed: () -> Unit) {

    BackHandler(enabled = true, onBack = {
        onBackPressed()
    })

}