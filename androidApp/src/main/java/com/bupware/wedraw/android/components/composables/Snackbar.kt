package com.bupware.wedraw.android.components.composables

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun SnackbarWrapper() {

    // Variable de estado para mantener la referencia al CoroutineScope
    var snackbarScope by remember { mutableStateOf(CoroutineScope(Dispatchers.Default)) }

    //Una animación del snackbar que se mostrará cuando haya un mensaje.
    //Pasado un delay manual borramos el mensaje para deejar el snackbar reutilizable

    AnimatedVisibility(
        visible = SnackbarManager.snackbarMessage.value.isNotBlank(),
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically() + fadeOut()
    ) {

        Snackbar( modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp), backgroundColor = SnackbarManager.snackbarColor.value)
        {
            Text(text = SnackbarManager.snackbarMessage.value)
        }

        // Cancelar el trabajo anterior antes de iniciar uno nuevo
        snackbarScope.coroutineContext.cancelChildren()
        snackbarScope.launch {
            delay(3000)
            SnackbarManager.removeSnackbar()
        }
    }
}

class SnackbarManager(){
    companion object{

        //region control del snackbar
        var snackbarMessage = mutableStateOf("")
        var snackbarColor = mutableStateOf(Color.DarkGray)
        fun newSnackbar(message:String, color: Color = Color.DarkGray){
            snackbarMessage.value = ""
            snackbarMessage.value = message
            snackbarColor.value = color
        }
        fun removeSnackbar(){
            snackbarMessage.value = ""
        }
        //endregion

    }
}