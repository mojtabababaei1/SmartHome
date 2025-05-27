package com.maadiran.myvision.domain.tv

interface IPairingManager {
    fun on(event: String, listener: () -> Unit)  // Changed to Function0
    suspend fun start(): Boolean
    fun sendCode(code: String): Boolean  // Removed suspend
    fun stop()
}