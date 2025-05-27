package com.maadiran.myvision.data.devices.tv.remote

import com.maadiran.myvision.data.proto.pairing.RemoteAppLinkLaunchRequest
import com.maadiran.myvision.data.proto.pairing.RemoteConfigure
import com.maadiran.myvision.data.proto.pairing.RemoteDeviceInfo
import com.maadiran.myvision.data.proto.pairing.RemoteDirection
import com.maadiran.myvision.data.proto.pairing.RemoteKeyCode
import com.maadiran.myvision.data.proto.pairing.RemoteKeyInject
import com.maadiran.myvision.data.proto.pairing.RemoteMessage
import com.maadiran.myvision.data.proto.pairing.RemotePingResponse
import com.maadiran.myvision.data.proto.pairing.RemoteSetActive


class RemoteMessageManager {

    var manufacturer: String = "Android"
    var model: String = android.os.Build.MODEL ?: "Unknown"

    fun createRemoteConfigure(): RemoteMessage {
        val deviceInfo = RemoteDeviceInfo.newBuilder()
            .setModel(model)
            .setVendor(manufacturer)
            .setUnknown1(1)
            .setUnknown2("1")
            .setPackageName("androidtv-remote")
            .setAppVersion("1.0.0")
            .build()
        val remoteConfigure = RemoteConfigure.newBuilder()
            .setCode1(622)
            .setDeviceInfo(deviceInfo)
            .build()
        return RemoteMessage.newBuilder()
            .setRemoteConfigure(remoteConfigure)
            .build()
    }

    fun createRemoteSetActive(active: Int): RemoteMessage {
        val remoteSetActive = RemoteSetActive.newBuilder()
            .setActive(active)
            .build()
        return RemoteMessage.newBuilder()
            .setRemoteSetActive(remoteSetActive)
            .build()
    }

    fun createRemotePingResponse(val1: Int): RemoteMessage {
        val remotePingResponse = RemotePingResponse.newBuilder()
            .setVal1(val1)
            .build()
        return RemoteMessage.newBuilder()
            .setRemotePingResponse(remotePingResponse)
            .build()
    }

    fun createRemoteKeyInject(direction: RemoteDirection, keyCode: RemoteKeyCode): RemoteMessage {
        val remoteKeyInject = RemoteKeyInject.newBuilder()
            .setDirection(direction)
            .setKeyCode(keyCode)
            .build()
        return RemoteMessage.newBuilder()
            .setRemoteKeyInject(remoteKeyInject)
            .build()
    }

    fun createRemoteAppLinkLaunchRequest(appLink: String): RemoteMessage {
        val remoteAppLinkLaunchRequest = RemoteAppLinkLaunchRequest.newBuilder()
            .setAppLink(appLink)
            .build()
        return RemoteMessage.newBuilder()
            .setRemoteAppLinkLaunchRequest(remoteAppLinkLaunchRequest)
            .build()
    }

    fun parse(buffer: ByteArray): RemoteMessage {
        return RemoteMessage.parseFrom(buffer)
    }
}
