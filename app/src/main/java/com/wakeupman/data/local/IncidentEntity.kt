package com.wakeupman.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidents")
data class IncidentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val triggerType: String, // "EYES_CLOSED" or "HEAD_NOD"
    val baselineUsed: Float
)
