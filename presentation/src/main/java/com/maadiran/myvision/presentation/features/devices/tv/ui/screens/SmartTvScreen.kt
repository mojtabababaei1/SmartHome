import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.maadiran.myvision.domain.services.IVoiceServiceManager
import com.maadiran.myvision.presentation.features.devices.tv.ui.screens.MainApp
import com.maadiran.myvision.presentation.features.devices.tv.viewmodels.MainViewModel

@SuppressLint("NewApi")
@Composable
fun SmartTvScreen(
    viewModel: MainViewModel,
    voiceServiceManager: IVoiceServiceManager
) {
    MainApp(
        mainViewModel = viewModel,
        voiceServiceManager = voiceServiceManager
    )
}
