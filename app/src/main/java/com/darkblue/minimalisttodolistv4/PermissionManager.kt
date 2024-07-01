package com.darkblue.minimalisttodolistv4

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.darkblue.minimalisttodolistv4.presentation.viewmodel.AppEvent
import com.darkblue.minimalisttodolistv4.presentation.viewmodel.AppViewModel

class PermissionManager(
    private val context: Context,
    private val activity: Activity,
    private val postNotificationPermissionLauncher: ActivityResultLauncher<String>,
    private val onTaskEvent: (AppEvent) -> Unit
) {
    fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasPostNotificationPermission()) {
                postNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else if (!hasScheduleExactAlarmPermission()) {
                onTaskEvent(AppEvent.ShowScheduleExactAlarmPermissionDialog)
            }
        }
    }

    fun handlePostNotificationPermissionResult(isGranted: Boolean) {
        if (isGranted && !hasScheduleExactAlarmPermission()) {
            onTaskEvent(AppEvent.ShowScheduleExactAlarmPermissionDialog)
        }
    }

    private fun hasScheduleExactAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
            alarmManager?.canScheduleExactAlarms() == true
        } else {
            true
        }
    }

    private fun hasPostNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun requestScheduleExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            activity.startActivity(intent)
        }
    }
}
