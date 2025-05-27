import com.maadiran.myvision.domain.services.IVoiceServiceManager

class YourPresenterClass(
    private val voiceServiceManager: IVoiceServiceManager
) {
    fun handleVoiceCommand() {
        voiceServiceManager.initialize(
            onVoiceCommand = { keyCode ->
                // Handle the keyCode as Int
            },
            onUrlReceived = { url ->
                // Handle the URL
            }
        )
    }
}
