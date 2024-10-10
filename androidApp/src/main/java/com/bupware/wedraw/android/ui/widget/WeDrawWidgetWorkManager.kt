package com.bupware.wedraw.android.ui.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.image.ImageRepository
import java.time.Duration

class WeDrawWidgetWorkManager(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val imageDao = WDDatabase.getDatabase(context).imageDao()
    private val repository: ImageRepository = ImageRepository(imageDao = imageDao)

    //    val drawingImage = repository.getDrawingImage(0)
    override suspend fun doWork(): Result {
        updateWidgetData()
        return Result.success()
    }


    private suspend fun updateWidgetData() {
        GlanceAppWidgetManager(context = context).getGlanceIds(WeDrawWidgetConsolidator::class.java)
            .forEach { glanceId ->
                updateAppWidgetState(context = context, glanceId = glanceId) { prefs ->
//                    Log.i("WeDrawWidgetWorkManager", "updateWidgetData: ${image.uri}")
//                    prefs.setImageData(image)
                }

                WeDrawWidgetConsolidator().update(context, glanceId)
            }


    }

    companion object{
        private const val WDRAW_FETCH_WORKER ="WdFetchWorker"
        private const val WDRAW_WORK_REQ_PERIOD_MINUTES = 15L
        private const val WDRAW_WORK_REQ_INITIAL_DELAY_SECONDS = 20L


        fun startWDrawDataFetch(workManager: WorkManager){
            Log.i("WeDrawWidgetWorkManager", "startWDrawDataFetch: ")
            val constrains = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            // TODO("Poner en el worker que no funcione sin espacio en el dispositivo,no se puede guardar la imagen y avisar al usuario")

            val request = PeriodicWorkRequestBuilder<WeDrawWidgetWorkManager>(Duration.ofMinutes(WDRAW_WORK_REQ_PERIOD_MINUTES)).setConstraints(constrains)
                .setInitialDelay(Duration.ofSeconds(WDRAW_WORK_REQ_INITIAL_DELAY_SECONDS))
                .build()

            val operation = workManager.enqueueUniquePeriodicWork(WDRAW_FETCH_WORKER, androidx.work.ExistingPeriodicWorkPolicy.KEEP, request)
            Log.i("WeDrawWidgetWorkManager", "startWDrawDataFetch: ${operation}")
        }

        fun cancelWDrawingDataFetch(workManager: WorkManager){
            Log.i("WeDrawWidgetWorkManager", "cancelWDrawingDataFetch: ")
            workManager.cancelUniqueWork(WDRAW_FETCH_WORKER)
        }
    }



}
