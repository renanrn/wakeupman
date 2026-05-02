package com.wakeupman.service

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun startVigilance() {
        val intent = Intent(context, VigilanceService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun stopVigilance() {
        val intent = Intent(context, VigilanceService::class.java)
        context.stopService(intent)
    }
}
