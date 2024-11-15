package dev.mniak.apps.anniversary.util

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import dev.mniak.apps.anniversary.R
import dev.mniak.apps.anniversary.widget.DayOfYearBigWidget
import dev.mniak.apps.anniversary.widget.DayOfYearWidget

/**
 * Utility class containing methods managing application widgets
 *
 * @author Siarhei Liauko
 * @since 1.1.4
 */
object WidgetUtil {

    /**
     * Update existing widgets sending intent with action [AppWidgetManager.ACTION_APPWIDGET_UPDATE]
     *
     * @param context application context
     *
     * @author Siarhei Liauko
     * @since 1.1.4
     */
    fun updateWidgets(context: Context) {
        val widgetStatus = context.getSharedPreferences(
            context.getString(R.string.shared_preferences_name),
            Context.MODE_PRIVATE
        ).getBoolean(context.getString(R.string.widget_status_key), false)

        if (widgetStatus) {
            val widgetManager = AppWidgetManager.getInstance(context)
            var ids = widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearWidget::class.java))
            ids += widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearBigWidget::class.java))
            val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context.sendBroadcast(updateIntent)
        }
    }
}
