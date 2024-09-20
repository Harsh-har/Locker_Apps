package com.example.LockerApp

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.LockerApp.Adapters.ApplogoAdapter
import com.example.LockerApp.Adapters.ButtonAdapter
import com.example.LockerApp.Classes.AppLogoClass
import com.example.LockerApp.Classes.ButtonClass
import com.example.LockerApp.databinding.ActivityLockMainBinding

class LockMainActivity : AppCompatActivity(), ApplogoAdapter.OnSelectionChangedListener {

    private lateinit var binding: ActivityLockMainBinding
    private val TAG = "LockMainActivity"
    private lateinit var applogoAdapter: ApplogoAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val REQUEST_CODE_OVERLAY_PERMISSION = 100

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply window insets for proper layout
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppLockPreferences", MODE_PRIVATE)

        // Check overlay permission
        checkOverlayPermission()

        // Set up RecyclerView for category buttons
        val datalist = arrayListOf(
            ButtonClass("Social"),
            ButtonClass("Beauty"),
            ButtonClass("Sports"),
            ButtonClass("Payment"),
            ButtonClass("Cooks")
        )

        binding.rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rv.adapter = ButtonAdapter(datalist, this)

        // Get installed apps and set up RecyclerView for apps
        val appList = getInstalledApps()
        applogoAdapter = ApplogoAdapter(appList, this, this)
        binding.rvlogo.layoutManager = GridLayoutManager(this, 4)
        binding.rvlogo.adapter = applogoAdapter

        // Select all apps when the master checkbox is checked
        binding.checkBox2.setOnCheckedChangeListener { _, isChecked ->
            selectAllCheckboxes(isChecked)
        }

        // Handle the lock button click
        binding.button4.setOnClickListener {
            handleLockButtonClick()
        }
    }

    // Check if overlay permission is granted
    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            requestPermissionLauncher.launch(intent)
        } else {
            showLockScreen(this)
        }
    }

    // Handle overlay permission result
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (Settings.canDrawOverlays(this)) {
            showLockScreen(this)
        } else {
            Toast.makeText(this, "Overlay permission is required to lock apps", Toast.LENGTH_SHORT).show()
        }
    }

    // Select or deselect all apps
    private fun selectAllCheckboxes(checked: Boolean) {
        applogoAdapter.selectAll(checked)
    }

    // Handle lock button click to start AppLockService
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleLockButtonClick() {
        val selectedApps = applogoAdapter.getSelectedApps()
        if (selectedApps.isEmpty()) {
            Toast.makeText(this, "No apps selected", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "No apps selected")
            return
        }

        Log.d(TAG, "Selected apps for locking: $selectedApps")

        if (!checkUsageAccessPermission(this)) {
            Log.d(TAG, "Usage access permission not granted")
            return
        }

        // Save selected apps in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putStringSet("lockedApps", selectedApps.toSet())
        editor.apply()

        // Start the foreground service to monitor selected apps
        val intent = Intent(this, AppLockService::class.java)
        intent.putStringArrayListExtra("selectedApps", ArrayList(selectedApps))
        startForegroundService(intent)

        Toast.makeText(this, "Apps locked successfully", Toast.LENGTH_SHORT).show()
    }

    // Check if usage access permission is granted
    private fun checkUsageAccessPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        Log.d(TAG, "Checking usage access permission")

        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )

        return if (mode == AppOpsManager.MODE_ALLOWED) {
            true
        } else {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            false
        }
    }

    // Show lock screen (unchanged)
    private fun showLockScreen(context: Context) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = LayoutInflater.from(context)
        val lockScreenView = inflater.inflate(R.layout.activity_lock_screen, null)

        val lockButton = lockScreenView.findViewById<Button>(R.id.unlockButton)
        val pinInput = lockScreenView.findViewById<EditText>(R.id.pinInput)

        lockButton.setOnClickListener {
            val enteredPin = pinInput.text.toString()
            if (enteredPin == "1234") {
                windowManager.removeView(lockScreenView)
            } else {
                Toast.makeText(context, "Incorrect PIN", Toast.LENGTH_SHORT).show()
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
            WindowManager.LayoutParams.FORMAT_CHANGED
        )

        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        windowManager.addView(lockScreenView, params)

        pinInput.post {
            pinInput.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(pinInput, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    // Get installed apps (unchanged)
    @SuppressLint("QueryPermissionsNeeded")
    private fun getInstalledApps(): List<AppLogoClass> {
        val pm = packageManager
        val apps = mutableListOf<AppLogoClass>()
        val packs = pm.getInstalledPackages(0)

        for (pack in packs) {
            if ((pack.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0) {
                val appName = pack.applicationInfo.loadLabel(pm).toString()
                val appIcon = pack.applicationInfo.loadIcon(pm)
                val packageName = pack.packageName
                apps.add(AppLogoClass(appName, appIcon, packageName, false))
            }
        }
        return apps
    }

    override fun onSelectionChanged(selectedCount: Int) {
        binding.button4.text = "Lock ($selectedCount)"
    }
}
