package dev.mniak.apps.anniversary.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import dev.mniak.apps.anniversary.R
import dev.mniak.apps.anniversary.receiver.DayOfYearUpdateReceiver
import dev.mniak.apps.anniversary.util.AlarmUtil

/**
 * Service class handling app notification and widget statuses
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class ApplicationToolsStatusService(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.shared_preferences_name),
        Context.MODE_PRIVATE
    )
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val alarmIntent = Intent(context, DayOfYearUpdateReceiver::class.java).let {
        PendingIntent.getBroadcast(context, 0, it, PendingIntent.FLAG_IMMUTABLE)
    }

    /**
     * Managing displaying app notification and alarm work
     *
     * @param status boolean parameter determined if notification displaying or not
     *
     * @author Siarhei Liauko
     * @since 1.0.0
     */
    fun updateNotificationStatus(status: Boolean) {
        val widgetStatus = sharedPreferences.getBoolean(context.getString(R.string.widget_status_key), false)
        sharedPreferences.edit().putBoolean(context.getString(R.string.notification_status_key), status).apply()
        if (status) {
            ContextCompat.startForegroundService(context, Intent(context, DayOfYearForegroundService::class.java))
            if (!widgetStatus) {
                AlarmUtil.setMidnightAlarm(alarmManager, alarmIntent)
            }
        } else {
            context.stopService(Intent(context, DayOfYearForegroundService::class.java))
            if (!widgetStatus) {
                alarmManager.cancel(alarmIntent)
            }
        }
    }

    /**
     * Managing app widgets and alarm work
     *
     * @param status boolean parameter determined if app widget displaying or not
     *
     * @author Siarhei Liauko
     * @since 1.0.0
     */
    fun updateWidgetStatus(status: Boolean) {
        val notificationStatus = sharedPreferences.getBoolean(context.getString(R.string.notification_status_key), false)
        sharedPreferences.edit().putBoolean(context.getString(R.string.widget_status_key), status).apply()
        if (!notificationStatus) {
            if (status) {
                AlarmUtil.setMidnightAlarm(alarmManager, alarmIntent)
            } else {
                alarmManager.cancel(alarmIntent)
            }
        }
    }
}
