package com.maadiran.myvision.data.devices.tv.pairing

import com.maadiran.myvision.data.proto.pairing.PairingConfiguration
import com.maadiran.myvision.data.proto.pairing.PairingEncoding
import com.maadiran.myvision.data.proto.pairing.PairingMessage
import com.maadiran.myvision.data.proto.pairing.PairingOption
import com.maadiran.myvision.data.proto.pairing.PairingRequest
import com.maadiran.myvision.data.proto.pairing.PairingSecret
import com.maadiran.myvision.data.proto.pairing.RoleType

class PairingMessageManager {

    var manufacturer: String = "Android"
    var model: String = android.os.Build.MODEL ?: "Unknown"

    fun createPairingRequest(serviceName: String): PairingMessage {
        val pairingRequest = PairingRequest.newBuilder()
            .setServiceName(serviceName)
            .setClientName(model)
            .build()
        return PairingMessage.newBuilder()
            .setPairingRequest(pairingRequest)
            .setStatus(PairingMessage.Status.STATUS_OK)
            .setProtocolVersion(2)
            .build()
    }

    fun createPairingOption(): PairingMessage {
        val encoding = PairingEncoding.newBuilder()
            .setType(PairingEncoding.EncodingType.ENCODING_TYPE_HEXADECIMAL)
            .setSymbolLength(6)
            .build()
        val pairingOption = PairingOption.newBuilder()
            .setPreferredRole(RoleType.ROLE_TYPE_INPUT)
            .addInputEncodings(encoding)
            .build()
        return PairingMessage.newBuilder()
            .setPairingOption(pairingOption)
            .setStatus(PairingMessage.Status.STATUS_OK)
            .setProtocolVersion(2)
            .build()
    }

    fun createPairingConfiguration(): PairingMessage {
        val encoding = PairingEncoding.newBuilder()
            .setType(PairingEncoding.EncodingType.ENCODING_TYPE_HEXADECIMAL)
            .setSymbolLength(6)
            .build()
        val pairingConfiguration = PairingConfiguration.newBuilder()
            .setClientRole(RoleType.ROLE_TYPE_INPUT)
            .setEncoding(encoding)
            .build()
        return PairingMessage.newBuilder()
            .setPairingConfiguration(pairingConfiguration)
            .setStatus(PairingMessage.Status.STATUS_OK)
            .setProtocolVersion(2)
            .build()
    }

    fun createPairingSecret(secret: ByteArray): PairingMessage {
        val pairingSecret = PairingSecret.newBuilder()
            .setSecret(com.google.protobuf.ByteString.copyFrom(secret))
            .build()
        return PairingMessage.newBuilder()
            .setPairingSecret(pairingSecret)
            .setStatus(PairingMessage.Status.STATUS_OK)
            .setProtocolVersion(2)
            .build()
    }

    fun parse(buffer: ByteArray): PairingMessage {
        return PairingMessage.parseFrom(buffer)
    }
}
