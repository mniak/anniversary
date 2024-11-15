package dev.mniak.apps.anniversary.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.widget.RemoteViews
import android.widget.Toast
import dev.mniak.apps.anniversary.ApplicationConstants
import dev.mniak.apps.anniversary.R
import dev.mniak.apps.anniversary.service.ApplicationToolsStatusService
import dev.mniak.apps.anniversary.util.DateCount
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * Class managing app widget with size 1x1
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
open class DayOfYearWidget : AppWidgetProvider() {

    /**
     * Updates each active widget.
     *
     * @param context application context
     * @param appWidgetManager an [AppWidgetManager] object
     * @param appWidgetIds the application widget identifiers for which an update is needed
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    /**
     * Called when widget is removed.
     *
     * @param context application context
     * @param appWidgetIds the application widget identifiers which was removed
     */
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            context.getSharedPreferences(
                context.getString(R.string.widget_shared_preferences_name),
                Context.MODE_PRIVATE
            )
                .edit()
                .remove("${appWidgetId}_is_default")
                .remove("${appWidgetId}_form_name")
                .remove("${appWidgetId}_background_color")
                .remove("${appWidgetId}_opacity")
                .remove("${appWidgetId}_text_color")
                .apply()
        }
    }

    /**
     * Called when the first widget is created.
     *
     * @param context application context
     */
    override fun onEnabled(context: Context) {
        val widgetManager = AppWidgetManager.getInstance(context)
        var ids = widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearWidget::class.java))
        ids += widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearBigWidget::class.java))
        if (ids.size == 1) {
            ApplicationToolsStatusService(context).updateWidgetStatus(true)
        }
    }

    /**
     * Called when the last widget with particular class is disabled. Need to check if widgets with
     * other class still exist.
     *
     * @param context application context
     */
    override fun onDisabled(context: Context) {
        val widgetManager = AppWidgetManager.getInstance(context)
        var ids = widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearWidget::class.java))
        ids += widgetManager.getAppWidgetIds(ComponentName(context, DayOfYearBigWidget::class.java))
        if (ids.isEmpty()) {
            ApplicationToolsStatusService(context).updateWidgetStatus(false)
        }
    }

    /**
     * Copy current day of the year to clipboard and show toast with message.
     *
     * @param context application context
     * @param intent intent being received
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (COPY_ACTION == intent.action) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    context.getString(R.string.clipboard_day_of_year_label),
                    DateCount.getCount().toString()
                )
            )
            Toast.makeText(
                context,
                R.string.day_copied_toast_message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

const val COPY_ACTION = "COPY"

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, DayOfYearWidget::class.java))
    val remoteView = if (appWidgetId in ids) {
        RemoteViews(context.packageName, R.layout.widget_day_of_year)
    } else {
        RemoteViews(context.packageName, R.layout.widget_day_of_year_large)
    }

    val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.widget_shared_preferences_name),
        Context.MODE_PRIVATE
    )

    remoteView.setImageViewResource(
        R.id.widget_background,
        context.resources.getIdentifier(
            sharedPreferences.getString(
                "${appWidgetId}_form_name",
                context.resources.getResourceEntryName(R.drawable.widget_background_circle)
            ),
            "drawable",
            context.packageName
        )
    )
    remoteView.setTextViewText(
        R.id.widget_text,
        DateCount.getCount().toString()
    )

    val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
    val width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
    val height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)
    if (width != 0 || height != 0) {
        remoteView.setTextViewTextSize(
            R.id.widget_text,
            TypedValue.COMPLEX_UNIT_SP,
            (if (width < height) width else height) / 3f
        )
    }

    if (!sharedPreferences.getBoolean("${appWidgetId}_is_default", true)) {
        remoteView.setInt(
            R.id.widget_background,
            "setColorFilter",
            sharedPreferences.getInt(
                "${appWidgetId}_background_color",
                context.getColor(R.color.widgetBackground)
            )
        )
        remoteView.setInt(
            R.id.widget_background,
            "setImageAlpha",
            sharedPreferences.getInt(
                "${appWidgetId}_opacity",
                ApplicationConstants.OPACITY_MAX_VALUE
            )
        )
        remoteView.setTextColor(
            R.id.widget_text,
            sharedPreferences.getInt(
                "${appWidgetId}_text_color",
                context.getColor(R.color.widgetText)
            )
        )
    }

    val intent = Intent(context, DayOfYearWidget::class.java)
    intent.action = COPY_ACTION
    remoteView.setOnClickPendingIntent(
        R.id.widget_text,
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    )

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, remoteView)
}
