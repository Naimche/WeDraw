package com.bupware.wedraw.android.ui.drawingScreen


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.ui.widget.WeDrawPreferences
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.DrawBoxPayLoad
import io.ak1.drawbox.rememberDrawController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun PreviewDrawingScreen() {
    DrawingScreen(rememberNavController())
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DrawingScreen(navController: NavController) {
    DrawingScreenBody(navController)
}

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DrawingScreenBody(
    navController: NavController,
    viewModel: DrawingScreenViewModel = hiltViewModel()
) {
    val controller = rememberDrawController()

    //Canvas container

    var a: DrawBoxPayLoad = DrawBoxPayLoad(Color.Black, emptyList())
    var b: Int
    var context = LocalContext.current
    val dataStore = WeDrawPreferences(context)

    var scope = rememberCoroutineScope()
    var uri by remember{mutableStateOf ("")}


    scope.launch {
        dataStore.setUri("asdadasdasda")
        dataStore.getUri.collect {
            uri = it
        }
    }



    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier.size(500.dp, 500.dp)

        ) {
            //DrawableBox(drawableController = rememberDrawableController())
            DrawBox(drawController = controller, bitmapCallback = processImage())
        }



        Column(Modifier.height(IntrinsicSize.Max)) {
            Button(onClick = { controller.unDo()}) {
                Text(text = "undo")
            }

            Button(onClick = { controller.reset() }) {
                Text(text = "reset")
            }

            Button(onClick = {
                controller.saveBitmap()
                //a = controller.exportPath()

            }) {
                Text(text = "export" + " ${uri}")

            }

            Button(onClick = { controller.importPath(a) }) {
                Text(text = "import")
            }


        }

        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Text(text = "")
            Image(bitmap = bebe.value.asImageBitmap(), contentDescription = "")
        }


    }


}

var bebe = mutableStateOf(createBitmap(1, 1))

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun processImage(viewModel: DrawingScreenViewModel = hiltViewModel()): (ImageBitmap?, Throwable?) -> Unit {
    val context = LocalContext.current
    return { imageBitmap, error ->

        if (imageBitmap != null) {
            val a = convertImageBitmapToBitmap(bitmap = imageBitmap)
            bebe.value = a
        }

        if (imageBitmap != null) {
            viewModel.imageBitmap = imageBitmap

            viewModel.insertData(
                context,
                viewModel.imageBitmap.asAndroidBitmap()
            )
        } else {
        }


    }
}


fun convertImageBitmapToBitmap(bitmap: ImageBitmap): Bitmap {
    return bitmap.asAndroidBitmap()
//    return Bitmap.createScaledBitmap(bitmap.asAndroidBitmap(), 500, 500,false)
}
