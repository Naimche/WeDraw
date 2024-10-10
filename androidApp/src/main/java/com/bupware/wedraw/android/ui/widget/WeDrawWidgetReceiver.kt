package com.bupware.wedraw.android.ui.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeDrawWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = WeDrawWidgetConsolidator()
    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        Log.i("WeDrawWidgetReceiver", "onEnabled")
        WeDrawWidgetWorkManager.startWDrawDataFetch(WorkManager.getInstance(context))
    }


    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        WeDrawWidgetWorkManager.cancelWDrawingDataFetch(WorkManager.getInstance(context))
    }
}
