
package com.maadiran.myvision.core.network.discovery

import com.maadiran.myvision.core.model.DeviceInfo

interface DeviceUpdateListener {
    fun onDevicesUpdated(devices: List<DeviceInfo>)
}