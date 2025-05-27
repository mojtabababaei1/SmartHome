package com.maadiran.myvision.core.model

data class DeviceInfo(
    val name: String,
    val host: String,
    val port: Int,
    val manufacturer: String? = null,
    val model: String? = null,
    val deviceType: String? = null,
    val serviceId: String? = null,
    val discoveryMethod: DiscoveryMethod = DiscoveryMethod.UNKNOWN
) {
    enum class DiscoveryMethod {
        MDNS,
        NSD,
        CAST,
        NETWORK_SCAN,
        UNKNOWN
    }

    companion object {
        fun createDefault(
            name: String = "Unknown Device",
            host: String,
            port: Int = 6466
        ): DeviceInfo = DeviceInfo(
            name = name,
            host = host,
            port = port
        )
    }
}
