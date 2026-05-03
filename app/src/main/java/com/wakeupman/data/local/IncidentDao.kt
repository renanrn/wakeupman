package com.wakeupman.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {
    @Query("SELECT * FROM incidents ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<IncidentEntity>>

    @Insert
    suspend fun insertIncident(incident: IncidentEntity)

    @Query("DELETE FROM incidents")
    suspend fun clearAll()
}
