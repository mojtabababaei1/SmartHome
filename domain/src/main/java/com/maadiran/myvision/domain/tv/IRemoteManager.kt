package com.maadiran.myvision.domain.tv

import com.maadiran.myvision.domain.model.RemoteKeyCode

interface IRemoteManager {
    fun initialize(host: String, port: Int, certificates: Map<String, String>)
    suspend fun start(): Boolean
    fun stop()
    fun on(event: String, listener: (Any?) -> Unit)
    fun sendPower()
    fun sendKey(keyCode: RemoteKeyCode)
    fun sendAppLink(appLink: String)
}