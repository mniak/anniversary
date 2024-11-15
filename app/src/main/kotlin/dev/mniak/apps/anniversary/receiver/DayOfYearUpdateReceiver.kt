package dev.mniak.apps.anniversary.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.mniak.apps.anniversary.util.AlarmUtil
import dev.mniak.apps.anniversary.util.NotificationUtil
import dev.mniak.apps.anniversary.util.WidgetUtil

/**
 * Class handling updating current day of the year for app notification and widgets
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class DayOfYearUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.updateNotification(context)
        WidgetUtil.updateWidgets(context)
        AlarmUtil.setMidnightAlarm(context)
    }
}
