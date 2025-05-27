package com.maadiran.myvision.data.service



class VoiceServiceManagerImpl : IVoiceServiceManager {

    private var onVoiceCommand: ((Int) -> Unit)? = null
    private var onUrlReceived: ((String) -> Unit)? = null

    override fun initialize(
        onVoiceCommand: (Int) -> Unit,
        onUrlReceived: (String) -> Unit
    ) {
        this.onVoiceCommand = onVoiceCommand
        this.onUrlReceived = onUrlReceived
    }

    override fun startListening() {
        // اینجا فعلا تستی یه داده می‌فرستیم
        onVoiceCommand?.invoke(1) // مثلا فرمان volume up
        onUrlReceived?.invoke("http://example.com/video.mp4")
    }

    override fun stopListening() {
        // اینجا می‌تونی میکروفن رو خاموش کنی
    }

    override fun destroy() {
        // پاک‌سازی منابع
    }
}
