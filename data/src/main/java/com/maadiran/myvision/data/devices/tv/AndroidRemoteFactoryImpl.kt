package com.maadiran.myvision.data.devices.tv

import com.maadiran.myvision.data.devices.tv.pairing.PairingManager
import com.maadiran.myvision.domain.tv.AndroidRemote
import com.maadiran.myvision.domain.tv.AndroidRemoteFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidRemoteFactoryImpl @Inject constructor(
    private val remoteManagerImpl: RemoteManagerImpl  // Inject RemoteManagerImpl
) : AndroidRemoteFactory {
    override fun create(host: String, options: AndroidRemote.Options, isPaired: Boolean): AndroidRemote {
        return AndroidRemote(
            host = host,
            options = options,
            isPaired = isPaired,
            pairingManagerFactory = { h, p, c, s -> PairingManager(h, p, c, s) },
            remoteManagerFactory = { h, p, c -> 
                remoteManagerImpl.apply { 
                    initialize(h, p, c)
                }
            }
        )
    }
}
