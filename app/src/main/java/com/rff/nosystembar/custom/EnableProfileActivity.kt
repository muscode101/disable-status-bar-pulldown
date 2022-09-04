package com.rff.nosystembar.custom

import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.rff.nosystembar.MainActivity
import com.rff.nosystembar.R

/**
 * This activity is started after the provisioning is complete in
 * [EnforcerDeviceAdminReceiver].
 */
class EnableProfileActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (null == savedInstanceState) {
            // Enable the newly created profile
            val manager = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val componentName = componentName
            manager.setProfileName(componentName, getString(R.string.app_name))
            manager.setProfileEnabled(componentName)
        }
        // Open the main screen
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}