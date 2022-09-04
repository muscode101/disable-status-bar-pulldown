package com.rff.nosystembar


import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.rff.nosystembar.custom.StatusBarOverlay
import com.rff.nosystembar.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (Settings.canDrawOverlays(this)){
            applyOverLay()
        }
        println("hasOverPermission::${Settings.canDrawOverlays(this)}")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        requestOverlayPermissionSystemAndApplyOverlay()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestOverlayPermissionSystemAndApplyOverlay(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                resultLauncher.launch(intent)
            }else{
                applyOverLay()
            }
        } else {
            val intent = Intent(this, Service::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun applyOverLay() {
        val manager = this.applicationContext
            .getSystemService(WINDOW_SERVICE) as WindowManager
        val activity = this@MainActivity
        val localLayoutParams = WindowManager.LayoutParams()
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
        localLayoutParams.gravity = Gravity.TOP
        localLayoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or  // this is to enable the notification to recieve touch events
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or  // Draws over status bar
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        val resId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        var result = 0
        if (resId > 0) {
            result = activity.resources.getDimensionPixelSize(resId)
        }
        localLayoutParams.height = result
        localLayoutParams.format = PixelFormat.TRANSPARENT
        val view = StatusBarOverlay(this)
        manager.addView(view, localLayoutParams)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}