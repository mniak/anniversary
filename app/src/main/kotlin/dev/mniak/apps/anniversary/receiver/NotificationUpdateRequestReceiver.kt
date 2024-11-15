package dev.mniak.apps.anniversary.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.mniak.apps.anniversary.ApplicationConstants
import dev.mniak.apps.anniversary.util.AlarmUtil
import dev.mniak.apps.anniversary.util.NotificationUtil

/**
 * Class handling case when device was rebooted or application updated
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class NotificationUpdateRequestReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (ApplicationConstants.notificationUpdateRequiredActions.contains(intent.action)) {
            NotificationUtil.restartNotification(context)
            AlarmUtil.setMidnightAlarm(context)
        }
    }
}
