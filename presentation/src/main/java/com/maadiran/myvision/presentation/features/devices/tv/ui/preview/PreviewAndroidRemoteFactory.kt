package com.maadiran.myvision.presentation.features.devices.tv.ui.preview

import com.maadiran.myvision.domain.tv.AndroidRemote
import com.maadiran.myvision.domain.tv.AndroidRemoteFactory

class PreviewAndroidRemoteFactory : AndroidRemoteFactory {
    override fun create(host: String, options: AndroidRemote.Options, isPaired: Boolean): AndroidRemote {
        return AndroidRemote(
            host = host,
            options = options,
            isPaired = isPaired,
            pairingManagerFactory = { _, _, _, _ -> PreviewPairingManager() },
            remoteManagerFactory = { _, _, _ -> PreviewRemoteManager() }
        )
    }
}
