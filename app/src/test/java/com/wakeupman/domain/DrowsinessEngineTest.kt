package com.wakeupman.domain

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DrowsinessEngineTest {

    private lateinit var engine: DrowsinessEngine

    @Before
    fun setUp() {
        engine = DrowsinessEngine()
    }

    @Test
    fun `processFaceData keeps history within 1 second`() {
        engine.processFaceData(10f, 1000L)
        engine.processFaceData(12f, 1500L)
        assertEquals(2, engine.getHistorySize())

        // Add an entry after 1 second, the first one should be removed
        engine.processFaceData(15f, 2500L)
        assertEquals(2, engine.getHistorySize())
    }

    @Test
    fun `processFaceData detects sudden drop greater than 30 degrees`() {
        engine.processFaceData(10f, 1000L) // Head slightly up
        
        // This simulates a sudden nod down to -25 degrees within 1s
        engine.processFaceData(-25f, 1500L) 
        
        // Pitch drop = 10 - (-25) = 35 > 30.
        // It triggers an emergency and clears the history to avoid multiple triggers.
        assertEquals(0, engine.getHistorySize())
    }

    @Test
    fun `processFaceData does not detect gradual drop`() {
        engine.processFaceData(10f, 1000L)
        
        // Simulates gradual movement, drops to -10, change is 20
        engine.processFaceData(-10f, 1500L) 
        assertEquals(2, engine.getHistorySize())
        
        // This drops to -25 but happens after 1.5 seconds from the first entry
        // The first entry (10f at 1000L) will be removed because 2600 - 1000 = 1600 > 1000.
        // The max pitch in window is now -10f. 
        // Drop is -10 - (-25) = 15 < 30.
        engine.processFaceData(-25f, 2600L) 
        
        // History should contain 2 items: the one at 1500L and the new one at 2600L.
        // And emergency is NOT triggered, history remains.
        assertEquals(2, engine.getHistorySize())
    }
}
