package com.maadiran.myvision.domain.utils

import com.maadiran.myvision.domain.model.RemoteKeyCode

object KeyCodeMapper {
    fun fromInt(code: Int): RemoteKeyCode {
        return when (code) {
            0 -> RemoteKeyCode.KEYCODE_UNKNOWN
            3 -> RemoteKeyCode.KEYCODE_HOME
            4 -> RemoteKeyCode.KEYCODE_BACK
            19 -> RemoteKeyCode.KEYCODE_DPAD_UP
            20 -> RemoteKeyCode.KEYCODE_DPAD_DOWN
            21 -> RemoteKeyCode.KEYCODE_DPAD_LEFT
            22 -> RemoteKeyCode.KEYCODE_DPAD_RIGHT
            23 -> RemoteKeyCode.KEYCODE_DPAD_CENTER
            24 -> RemoteKeyCode.KEYCODE_VOLUME_UP
            25 -> RemoteKeyCode.KEYCODE_VOLUME_DOWN
            26 -> RemoteKeyCode.KEYCODE_POWER
            66 -> RemoteKeyCode.KEYCODE_ENTER
            82 -> RemoteKeyCode.KEYCODE_MENU
            164 -> RemoteKeyCode.KEYCODE_VOLUME_MUTE
            166 -> RemoteKeyCode.KEYCODE_CHANNEL_UP
            167 -> RemoteKeyCode.KEYCODE_CHANNEL_DOWN
            // Add other mappings as needed
            else -> RemoteKeyCode.KEYCODE_UNKNOWN
        }
    }
}
