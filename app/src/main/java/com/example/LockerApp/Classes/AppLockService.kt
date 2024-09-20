package com.example.LockerApp

import android.app.*
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AppLockService : Service() {
    private val TAG = "AppLockService"
    private var isLockScreenVisible = false
    private lateinit var selectedApps: List<String>
    private var lockScreenView: View? = null
    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler
    private val checkInterval: Long = 1000 // Check every second
    private var lastIncorrectPinTime: Long = 0
    private val lockDuration: Long = 30000 // 30 seconds lock period after incorrect PIN
    private var currentLockedApp: String? = null // Track the currently locked app

    override fun onCreate() {
        super.onCreate()

        // Create a notification channel for foreground service (required on Android O and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "AppLockServiceChannel",
                "App Lock Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        handlerThread = HandlerThread("AppLockServiceHandlerThread").apply { start() }
        handler = Handler(handlerThread.looper)
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        selectedApps = intent?.getStringArrayListExtra("selectedApps") ?: emptyList()
        Log.d(TAG, "Selected apps for locking: $selectedApps")

        if (!checkOverlayPermission() || !checkUsageAccessPermission()) {
            Log.e(TAG, "Required permissions not granted. Stopping service.")
            Toast.makeText(this, "Required permissions not granted", Toast.LENGTH_SHORT).show()
            stopSelf()
            return START_NOT_STICKY
        }

        handler.post(runnable)
        return START_STICKY
    }

    private val runnable = object : Runnable {
        override fun run() {
            try {
                val currentApp = getForegroundApp()
                Log.d(TAG, "Foreground app: $currentApp")

                if (currentApp != null && selectedApps.contains(currentApp)) {
                    Log.d(TAG, "App $currentApp is in the foreground")

                    if (currentLockedApp != currentApp) {
                        Log.d(TAG, "Locking new app: $currentApp")
                        currentLockedApp = currentApp
                        showLockScreen(currentApp)
                    }
                } else {
                    removeLockScreen()
                    currentLockedApp = null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error while checking foreground app: ${e.message}")
            }
            handler.postDelayed(this, checkInterval)
        }
    }

    private fun showLockScreen(app: String) {
        val currentTime = System.currentTimeMillis()

        // Prevent showing lock screen if the app is still within the lock period
        if (currentTime - lastIncorrectPinTime < lockDuration) {
            Log.d(TAG, "App is temporarily locked. Try again later.")
            return
        }

        if (isLockScreenVisible) {
            Log.d(TAG, "Lock screen already visible")
            return
        }

        Log.d(TAG, "Showing lock screen for app: $app")
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = LayoutInflater.from(this)
        lockScreenView = inflater.inflate(R.layout.activity_lock_screen, null)

        val lockButton = lockScreenView?.findViewById<Button>(R.id.unlockButton)
        val pinInput = lockScreenView?.findViewById<EditText>(R.id.pinInput)

        lockButton?.setOnClickListener {
            val enteredPin = pinInput?.text.toString()
            if (isValidPin(enteredPin)) {
                Log.d(TAG, "Correct PIN entered, unlocking app $app")
                removeLockScreen()
            } else {
                lastIncorrectPinTime = currentTime
                Toast.makeText(this, "Incorrect PIN. Try again in 30 seconds", Toast.LENGTH_SHORT).show()
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
        isLockScreenVisible = true

        pinInput?.postDelayed({
            pinInput.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(pinInput, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }

    private fun isValidPin(enteredPin: String): Boolean {
        return enteredPin == "1234" // Replace with secure PIN logic
    }

    private fun removeLockScreen() {
        if (lockScreenView != null && isLockScreenVisible) {
            Log.d(TAG, "Removing lock screen")
            val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            windowManager.removeView(lockScreenView)
            lockScreenView = null
            isLockScreenVisible = false
        }
    }

    private fun checkUsageAccessPermission(): Boolean {
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun checkOverlayPermission(): Boolean {
        return if (Settings.canDrawOverlays(this)) {
            true
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            false
        }
    }

    private fun getForegroundApp(): String? {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 10000 // Check last 10 seconds of usage
        val usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime)

        if (usageStats.isNotEmpty()) {
            val recentApp = usageStats.maxByOrNull { it.lastTimeUsed }
            return recentApp?.packageName
        }
        return null
    }

    private fun startForegroundService() {
        val notificationIntent = Intent(this, LockScreenActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, "AppLockServiceChannel")
            .setContentTitle("App Lock Service")
            .setContentText("Monitoring selected apps")
            .setSmallIcon(R.drawable.circle)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
        handlerThread.quitSafely()
        removeLockScreen()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
