package com.wakeupman.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.wakeupman.data.PreferencesRepository
import com.wakeupman.data.local.IncidentDao

@OptIn(ExperimentalCoroutinesApi::class)
class DrowsinessEngineTest {

    private lateinit var engine: DrowsinessEngine
    private val testDispatcher = StandardTestDispatcher()
    private val mockDao = mockk<IncidentDao>(relaxed = true)
    
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        val mockRepo = mockk<PreferencesRepository>()
        val flow = MutableStateFlow<Float?>(0.8f) // Default baseline 0.8
        every { mockRepo.eyeOpenBaselineFlow } returns flow
        
        engine = DrowsinessEngine(mockRepo, mockDao)
        engine.start() // Sets state to ACTIVE
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `processFaceData detects sudden pitch drop`() {
        engine.processFaceData(10f, null, null, 1000L) 
        engine.processFaceData(-25f, null, null, 1500L) 
        
        assertEquals(VigilanceState.EMERGENCY, engine.vigilanceState.value)
    }

    @Test
    fun `processFaceData detects eyes closed for more than 2 seconds`() {
        // threshold is 0.8 * 0.4 = 0.32
        engine.processFaceData(0f, 0.2f, 0.2f, 1000L) // Start closing
        assertEquals(VigilanceState.WARNING, engine.vigilanceState.value)
        
        engine.processFaceData(0f, 0.2f, 0.2f, 2000L) // Still closed (1s)
        assertEquals(VigilanceState.WARNING, engine.vigilanceState.value)
        
        engine.processFaceData(0f, 0.2f, 0.2f, 3100L) // Closed for 2.1s
        assertEquals(VigilanceState.EMERGENCY, engine.vigilanceState.value)
    }
    
    @Test
    fun `processFaceData recovers to ACTIVE if eyes open`() {
        engine.processFaceData(0f, 0.2f, 0.2f, 1000L) // Start closing
        assertEquals(VigilanceState.WARNING, engine.vigilanceState.value)
        
        engine.processFaceData(0f, 0.9f, 0.9f, 1500L) // Open again
        assertEquals(VigilanceState.ACTIVE, engine.vigilanceState.value)
    }
}
