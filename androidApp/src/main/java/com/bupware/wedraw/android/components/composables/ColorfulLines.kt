package com.bupware.wedraw.android.components.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bupware.wedraw.android.theme.blueWeDraw
import com.bupware.wedraw.android.theme.greenWeDraw
import com.bupware.wedraw.android.theme.redWeDraw
import com.bupware.wedraw.android.theme.yellowWeDraw

@Composable
fun ColorfulLines(height: Dp = 13.dp){
    Row(Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier
            .weight(1f)
            .background(blueWeDraw)
            .height(height))
        Spacer(modifier = Modifier
            .weight(1f)
            .background(greenWeDraw)
            .height(height))
        Spacer(modifier = Modifier
            .weight(1f)
            .background(yellowWeDraw)
            .height(height))
        Spacer(modifier = Modifier
            .weight(1f)
            .background(redWeDraw)
            .height(height))
    }
}