package com.maadiran.myvision.presentation.features.devices.washingmachine

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.maadiran.myvision.presentation.base.BaseSpeechActivity
import com.maadiran.myvision.data.local.PreferencesManager
import com.maadiran.myvision.presentation.features.devices.washingmachine.ui.screens.WashingMachineScreen
import com.maadiran.myvision.presentation.features.devices.washingmachine.viewmodels.WashingMachineViewModel
import com.maadiran.myvision.presentation.ui.theme.MyVisionTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WashingMachineActivity : BaseSpeechActivity() {
    private val TAG = "WM_DEBUG"
    private val viewModel: WashingMachineViewModel by viewModels()
    private val ip: String by lazy { intent.getStringExtra("ip") ?: "" }
    private val DEVICE_TYPE = "washing-machine"  // Add constant here too

    @Inject
    lateinit var preferencesManager: PreferencesManager
    override fun handleSpeechResult(result: Result<String>) {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ip.isNotEmpty()) {
            Log.d(TAG, "Saving IP $ip for device type $DEVICE_TYPE")
            preferencesManager.saveIpAddress(DEVICE_TYPE, ip)
            // Verify save
            val savedIp = preferencesManager.getIpAddress(DEVICE_TYPE)
            Log.d(TAG, "Verified saved IP: $savedIp")
        } else {
            Log.e(TAG, "No IP provided to WashingMachineActivity")
        }

//        setContent {
//            MyVisionTheme {
//                val navController = rememberNavController()
//
//                WashingMachineScreen(
//                    ip = ip,
//                    navController = navController,
//                    onSpeechButtonClick = { startSpeechRecognition() }
//                )
//            }
//        }
    }
}