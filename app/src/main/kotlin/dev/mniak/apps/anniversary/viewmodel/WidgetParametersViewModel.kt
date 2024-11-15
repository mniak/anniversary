package dev.mniak.apps.anniversary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.mniak.apps.anniversary.ApplicationConstants
import dev.mniak.apps.anniversary.R

/**
 * View model storing widget configuration parameters
 *
 * @author Siarhei Liauko
 * @since 1.1.4
 */
class WidgetParametersViewModel(application: Application) : AndroidViewModel(application) {
    var form: String =
        getApplication<Application>().applicationContext.resources.getResourceEntryName(R.drawable.widget_background_circle)
    var backgroundColor: Int =
        getApplication<Application>().applicationContext.getColor(R.color.widgetBackground)
    var opacity: Int = ApplicationConstants.OPACITY_MAX_VALUE
    var textColor: Int = getApplication<Application>().applicationContext.getColor(R.color.widgetText)
}
