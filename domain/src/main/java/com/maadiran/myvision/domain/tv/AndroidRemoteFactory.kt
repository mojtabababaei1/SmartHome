package com.maadiran.myvision.domain.tv

interface AndroidRemoteFactory {
    fun create(host: String, options: AndroidRemote.Options, isPaired: Boolean): AndroidRemote
}
