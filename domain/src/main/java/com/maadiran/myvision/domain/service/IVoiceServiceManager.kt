interface IVoiceServiceManager {
    fun initialize(onVoiceCommand: (Int) -> Unit, onUrlReceived: (String) -> Unit)
    fun startListening()
    fun stopListening()
    fun destroy()
}
