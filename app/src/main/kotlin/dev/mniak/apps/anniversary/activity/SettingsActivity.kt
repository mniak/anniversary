package dev.mniak.apps.anniversary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.mniak.apps.anniversary.R
import dev.mniak.apps.anniversary.activity.fragment.SettingsFragment
import dev.mniak.apps.anniversary.databinding.ActivitySettingsBinding

/**
 * Class managing settings screen
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.toolbar.setNavigationIcon(R.drawable.arrow_left)
        viewBinding.toolbar.setNavigationContentDescription(R.string.navigation_back_button_description)
        viewBinding.toolbar.setNavigationOnClickListener { finish() }

        supportFragmentManager.beginTransaction()
                .replace(R.id.settings_frame_container, SettingsFragment())
                .commit()
    }
}
