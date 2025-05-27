package com.maadiran.myvision.presentation.features.devices.tv.viewmodels

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maadiran.myvision.core.model.DeviceInfo
import com.maadiran.myvision.core.network.discovery.DeviceUpdateListener
import com.maadiran.myvision.core.network.discovery.SSDPDiscoveryManager
import com.maadiran.myvision.core.security.CertificateGenerator
import com.maadiran.myvision.domain.model.RemoteKeyCode
import com.maadiran.myvision.domain.services.IVoiceServiceManager
import com.maadiran.myvision.domain.tv.AndroidRemote
import com.maadiran.myvision.domain.tv.AndroidRemoteFactory
import com.maadiran.myvision.domain.tv.IRemoteManager
import com.maadiran.myvision.domain.utils.KeyCodeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SuppressLint("InlinedApi")
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val remoteManager: IRemoteManager,
    private val voiceServiceManager: IVoiceServiceManager,
    private val androidRemoteFactory: AndroidRemoteFactory  // Add this
) : ViewModel(), DeviceUpdateListener {
    private val TAG = "MainViewModel"
    private var ssdpDiscoveryManager: SSDPDiscoveryManager? = null
    private lateinit var androidRemote: AndroidRemote
    private val gson = Gson()

    fun initializeVoiceManager(context: Context) {
        voiceServiceManager.initialize(
            onVoiceCommand = { command ->
                // Convert int command to domain RemoteKeyCode
                sendKey(KeyCodeMapper.fromInt(command))
            },
            onUrlReceived = { url ->
                Log.d(TAG, "Received URL in MainViewModel: $url")
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        androidRemote.sendAppLink(url)
                        Log.d(TAG, "Successfully sent URL to TV: $url")
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to send URL to TV", e)
                        _connectionState.value = ConnectionState.Error("Failed to send URL: ${e.message}")
                    }
                }
            }
        )
    }

    // Update this method to return the interface type
    fun getVoiceServiceManager(): IVoiceServiceManager = voiceServiceManager

    // State management
    private val _isPaired = MutableStateFlow(false)
    val isPaired: StateFlow<Boolean> get() = _isPaired

    private val _isDiscovering = MutableStateFlow(false)
    val isDiscovering: StateFlow<Boolean> = _isDiscovering

    private val _discoveryError = MutableStateFlow<String?>(null)
    val discoveryError: StateFlow<String?> = _discoveryError

    // Default ports
    val defaultPairingPort = 6467
    val defaultRemotePort = 6466

    // TV host and ports
    private var tvHost: String = "192.168.1.105"
    private var pairingPort: Int = defaultPairingPort
    private var remotePort: Int = defaultRemotePort

    // Certificates
    private var certs: Map<String, String> = emptyMap()

    // Callback for pairing code
    var onPairingCodeRequested: (() -> Unit)? = null

    private val _discoveredDevices = MutableStateFlow<List<DeviceInfo>>(emptyList())
    val discoveredDevices: StateFlow<List<DeviceInfo>> = _discoveredDevices.asStateFlow()

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _responseMessages = MutableStateFlow<List<String>>(emptyList())
    val responseMessages: StateFlow<List<String>> get() = _responseMessages

    // Voice command receiver
    private val voiceCommandReceiver = object : BroadcastReceiver() {
        override fun onReceive(receiverContext: Context?, intent: Intent?) {
            val command = intent?.getStringExtra("command") ?: return
            val receivedContext = intent.getStringExtra("context") ?: "default"
            processVoiceCommand(command, receivedContext)
        }
    }

    sealed class ConnectionState {
        object Disconnected : ConnectionState()
        object Discovering : ConnectionState()
        object Connecting : ConnectionState()
        object Connected : ConnectionState()
        data class Error(val message: String) : ConnectionState()
    }

    init {
        initializeDiscoveryManager()
        loadPairingInfo()

        if (isPaired.value) {
            initializeAndroidRemote()
            startRemoteManager()
        } else {
            generateCertificates()
        }

        // Register voice receiver
        val filter = IntentFilter("com.maadiran.myvision.VOICE_COMMAND")
        context.registerReceiver(voiceCommandReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    private fun initializeDiscoveryManager() {
        ssdpDiscoveryManager = SSDPDiscoveryManager(
            context = context,
            coroutineScope = viewModelScope
        )

        // Collect devices emitted from the discovery manager
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ssdpDiscoveryManager?.discoveredDevicesFlow
                    ?.collect { deviceInfo ->
                        _discoveredDevices.update { devices ->
                            if (devices.any { it.host == deviceInfo.host }) {
                                devices // Device already exists
                            } else {
                                devices + deviceInfo // Add new device
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error collecting discovered devices", e)
            }
        }
    }

    fun startSsdpDiscovery() {
        viewModelScope.launch {
            try {
                _isDiscovering.value = true
                _discoveryError.value = null
                _connectionState.value = ConnectionState.Discovering

                Log.d(TAG, "Starting SSDP and mDNS discovery")
                ssdpDiscoveryManager?.discoverDevices()
            } catch (e: Exception) {
                Log.e(TAG, "Error during discovery", e)
                _discoveryError.value = "Discovery failed: ${e.message}"
                _connectionState.value = ConnectionState.Error(e.message ?: "Unknown error")
            } finally {
                _isDiscovering.value = false
            }
        }
    }
    fun onMicrophoneClicked() {
        Log.d("VoiceDebug", "Microphone button clicked")
        voiceServiceManager.startVoskVoice()
    }

    fun setDeviceInfo(host: String, pairingPort: Int?, remotePort: Int?) {
        viewModelScope.launch {
            try {
                _connectionState.value = ConnectionState.Connecting

                tvHost = host
                this@MainViewModel.pairingPort = pairingPort ?: defaultPairingPort
                this@MainViewModel.remotePort = remotePort ?: defaultRemotePort

                initializeAndroidRemote()

                Log.d(
                    TAG,
                    "Device info set - Host: $host, Pairing Port: ${this@MainViewModel.pairingPort}, Remote Port: ${this@MainViewModel.remotePort}"
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error setting device info", e)
                _connectionState.value =
                    ConnectionState.Error(e.message ?: "Failed to connect to device")
            }
        }
    }

    fun startPairing() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _connectionState.value = ConnectionState.Connecting
                val started = androidRemote.start()
                if (!isPaired.value && started) {
                    // Trigger the pairing code request callback
                    withContext(Dispatchers.Main) {
                        onPairingCodeRequested?.invoke()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error starting pairing", e)
                _connectionState.value =
                    ConnectionState.Error(e.message ?: "Failed to start pairing")
                withContext(Dispatchers.Main) {
                    _discoveryError.value = "Failed to start pairing: ${e.message}"
                }
            }
        }
    }

    fun sendPairingCode(code: String) {
        if (code.length != 6 || !code.all { it.isLetterOrDigit() }) {
            _discoveryError.value = "Invalid pairing code. Please enter 6 letters or digits."
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _connectionState.value = ConnectionState.Connecting
                val success = androidRemote.sendCode(code)

                withContext(Dispatchers.Main) {
                    if (success) {
                        _isPaired.value = true
                        _connectionState.value = ConnectionState.Connected
                        savePairingInfo()
                        startRemoteManager()
                    } else {
                        _connectionState.value = ConnectionState.Error("Invalid pairing code")
                        _discoveryError.value = "Invalid pairing code. Please try again."
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending pairing code", e)
                withContext(Dispatchers.Main) {
                    _connectionState.value =
                        ConnectionState.Error(e.message ?: "Failed to send pairing code")
                    _discoveryError.value = "Failed to send pairing code: ${e.message}"
                }
            }
        }
    }

    private fun startRemoteManager() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val connected = androidRemote.start()
                withContext(Dispatchers.Main) {
                    if (!connected) {
                        _isPaired.value = false
                        _discoveryError.value = "Failed to connect to remote"
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error starting remote manager", e)
            }
        }
    }

    fun processVoiceCommand(command: String, context: String) {
        when (context) {
            "controller" -> handleControllerCommands(command)
            else -> handleControllerCommands(command)
        }
    }

    private fun handleControllerCommands(command: String) {
        viewModelScope.launch {
            Log.d("MainViewModel", "Processing voice command: $command")
            when {
                command.contains("صدا") && command.contains("بالا") -> {
                    sendKey(RemoteKeyCode.KEYCODE_VOLUME_UP)
                    sendRandomResponse("volume_up")
                }

                command.contains("صدا") && command.contains("پایین") -> {
                    sendKey(RemoteKeyCode.KEYCODE_VOLUME_DOWN)
                    sendRandomResponse("volume_down")
                }

                command.contains("کانال") && command.contains("بالا") -> {
                    sendKey(RemoteKeyCode.KEYCODE_CHANNEL_UP)
                    sendRandomResponse("channel_up")
                }

                command.contains("کانال") && command.contains("پایین") -> {
                    sendKey(RemoteKeyCode.KEYCODE_CHANNEL_DOWN)
                    sendRandomResponse("channel_down")
                }

                command.contains("بی‌صدا") || command.contains("بی صدا") -> {
                    sendKey(RemoteKeyCode.KEYCODE_MUTE)
                    sendRandomResponse("mute")
                }

                command.contains("خاموش") || command.contains("روشن") -> {
                    sendPower()
                    sendRandomResponse("power")
                }

                else -> {
                    sendRandomResponse("unknown_command")
                }
            }
        }
    }

    private fun sendRandomResponse(type: String) {
        val responses = responseMap[type] ?: listOf("دستور انجام شد")
        val response = responses.random()
        _responseMessages.value = _responseMessages.value + response
    }

    private val responseMap = mapOf(
        "volume_up" to listOf("صدا افزایش یافت", "بلندتر شد", "صدای تلویزیون بالا رفت"),
        "volume_down" to listOf("صدا کاهش یافت", "آهسته‌تر شد", "صدای تلویزیون پایین آمد"),
        "channel_up" to listOf("کانال بعدی", "کانال بالا رفت", "به کانال بعدی رفتیم"),
        "channel_down" to listOf("کانال قبلی", "کانال پایین آمد", "به کانال قبلی رفتیم"),
        "mute" to listOf("بی‌صدا شد", "صدا قطع شد", "تلویزیون در حالت سکوت قرار گرفت"),
        "power" to listOf("دستگاه خاموش شد", "تلویزیون روشن/خاموش شد"),
        "unknown_command" to listOf("دستور نامفهوم بود", "متوجه نشدم، لطفاً دوباره بگویید")
    )

    private fun generateCertificates() {
        certs = CertificateGenerator.generateFull(
            name = "Android Remote",
            country = "Country",
            state = "YourState",
            locality = "YourCity",
            organisation = "YourOrganisation",
            OU = "YourOU"
        )
    }

    fun initializeAndroidRemote() {
        val options = AndroidRemote.Options(
            cert = certs,
            pairingPort = pairingPort,
            remotePort = remotePort,
            serviceName = "androidtvremote"
        )

        androidRemote = androidRemoteFactory.create(tvHost, options, isPaired.value)
        setupAndroidRemoteListeners()
        
        // Initialize RemoteManager through the interface
        remoteManager.initialize(
            host = tvHost,
            port = remotePort,
            certificates = certs
        )
    }

    private fun setupAndroidRemoteListeners() {
        androidRemote.on("ready") {
            viewModelScope.launch(Dispatchers.Main) {
                _isPaired.value = true
                savePairingInfo()
            }
        }

        androidRemote.on("error") { error ->
            viewModelScope.launch(Dispatchers.Main) {
                _isPaired.value = false
                _connectionState.value = ConnectionState.Error(error?.toString() ?: "Unknown error")
            }
        }

        androidRemote.on("unpaired") {
            viewModelScope.launch(Dispatchers.Main) {
                _isPaired.value = false
                clearPairingInfo()
            }
        }

        androidRemote.on("secret") {
            viewModelScope.launch(Dispatchers.Main) {
                onPairingCodeRequested?.invoke()
            }
        }
    }

    fun sendKey(keyCode: RemoteKeyCode) {
        viewModelScope.launch {
            remoteManager.sendKey(keyCode)
        }
    }

    fun sendPower() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                androidRemote.sendPower()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error sending power command", e)
                withContext(Dispatchers.Main) {
                    _connectionState.value = ConnectionState.Error("Failed to send power command")
                }
            }
        }
    }

    // Save pairing info to SharedPreferences
    private fun savePairingInfo() {
        val editor = sharedPreferences.edit()
        editor.putString("host", tvHost)
        editor.putInt("pairingPort", pairingPort)
        editor.putInt("remotePort", remotePort)
        val certsJson = gson.toJson(certs)
        editor.putString("certs", certsJson)
        editor.putBoolean("isPaired", true)
        editor.apply()
    }

    // Load pairing info from SharedPreferences
    private fun loadPairingInfo() {
        val isPairedStored = sharedPreferences.getBoolean("isPaired", false)
        if (isPairedStored) {
            tvHost = sharedPreferences.getString("host", "") ?: ""
            pairingPort = sharedPreferences.getInt("pairingPort", defaultPairingPort)
            remotePort = sharedPreferences.getInt("remotePort", defaultRemotePort)
            val certsJson = sharedPreferences.getString("certs", null)
            if (certsJson != null) {
                // Create type token as an anonymous class instance
                val mapType = object : TypeToken<HashMap<String, String>>() {}.type
                try {
                    certs = gson.fromJson(certsJson, mapType)
                } catch (e: Exception) {
                    Log.e(TAG, "Error deserializing certs", e)
                    generateCertificates() // Fallback to generating new certs
                }
            } else {
                generateCertificates()
            }
            _isPaired.value = true
        }
    }

    // Clear pairing info from SharedPreferences
    private fun clearPairingInfo() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


    fun getLastPairedDeviceInfo(): Pair<String, Map<String, String>>? {
        val isPairedStored = sharedPreferences.getBoolean("isPaired", false)
        if (!isPairedStored) return null

        val host = sharedPreferences.getString("host", null)
        val certsJson = sharedPreferences.getString("certs", null)

        if (host.isNullOrEmpty() || certsJson.isNullOrEmpty()) return null

        val type = object : TypeToken<Map<String, String>>() {}.type
        val certs: Map<String, String> = try {
            gson.fromJson(certsJson, type)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing stored certificates", e)
            return null
        }

        return host to certs
    }


    override fun onCleared() {
        viewModelScope.launch {
            if (isPaired.value) {
                androidRemote.stop()
            }
            context.unregisterReceiver(voiceCommandReceiver)
            voiceServiceManager.destroy()  // No need for null check now
        }
        super.onCleared()
    }

    fun sendAppLink(appLink: String) {
        viewModelScope.launch {
            try {
                androidRemote.sendAppLink(appLink)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error sending app link", e)
                _connectionState.value = ConnectionState.Error("Failed to send app link")
            }
        }
    }

    override fun onDevicesUpdated(devices: List<DeviceInfo>) {
        // Implement your device update logic here
        // For example:
        ///_devices.value = devices
    }
}
