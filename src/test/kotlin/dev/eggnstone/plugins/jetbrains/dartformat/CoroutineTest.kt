package dev.eggnstone.plugins.jetbrains.dartformat

import kotlinx.coroutines.runBlocking
import org.junit.Test

class CoroutineTest {
    @Test
    fun testCoroutineWorks() {
        runBlocking {
            // This will fail if the SpillingKt class is missing
            println("[DEBUG_LOG] Coroutine test running successfully")
        }
    }
}