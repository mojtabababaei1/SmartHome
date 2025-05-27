package com.maadiran.myvision.data.mapper

import com.maadiran.myvision.domain.model.RemoteKeyCode as DomainKeyCode
import com.maadiran.myvision.data.proto.pairing.RemoteKeyCode as ProtoKeyCode

object RemoteKeyCodeMapper {
    fun mapToDomain(protoKeyCode: ProtoKeyCode): DomainKeyCode = when (protoKeyCode) {
        // System Keys
        ProtoKeyCode.KEYCODE_UNKNOWN -> DomainKeyCode.KEYCODE_UNKNOWN
        ProtoKeyCode.KEYCODE_POWER -> DomainKeyCode.KEYCODE_POWER
        ProtoKeyCode.KEYCODE_HOME -> DomainKeyCode.KEYCODE_HOME
        ProtoKeyCode.KEYCODE_BACK -> DomainKeyCode.KEYCODE_BACK
        ProtoKeyCode.KEYCODE_MENU -> DomainKeyCode.KEYCODE_MENU
        ProtoKeyCode.KEYCODE_SETTINGS -> DomainKeyCode.KEYCODE_SETTINGS

        // Navigation
        ProtoKeyCode.KEYCODE_DPAD_UP -> DomainKeyCode.KEYCODE_DPAD_UP
        ProtoKeyCode.KEYCODE_DPAD_DOWN -> DomainKeyCode.KEYCODE_DPAD_DOWN
        ProtoKeyCode.KEYCODE_DPAD_LEFT -> DomainKeyCode.KEYCODE_DPAD_LEFT
        ProtoKeyCode.KEYCODE_DPAD_RIGHT -> DomainKeyCode.KEYCODE_DPAD_RIGHT
        ProtoKeyCode.KEYCODE_DPAD_CENTER -> DomainKeyCode.KEYCODE_DPAD_CENTER
        ProtoKeyCode.KEYCODE_DPAD_UP_LEFT -> DomainKeyCode.KEYCODE_DPAD_UP_LEFT
        ProtoKeyCode.KEYCODE_DPAD_DOWN_LEFT -> DomainKeyCode.KEYCODE_DPAD_DOWN_LEFT
        ProtoKeyCode.KEYCODE_DPAD_UP_RIGHT -> DomainKeyCode.KEYCODE_DPAD_UP_RIGHT
        ProtoKeyCode.KEYCODE_DPAD_DOWN_RIGHT -> DomainKeyCode.KEYCODE_DPAD_DOWN_RIGHT

        // Volume Controls
        ProtoKeyCode.KEYCODE_VOLUME_UP -> DomainKeyCode.KEYCODE_VOLUME_UP
        ProtoKeyCode.KEYCODE_VOLUME_DOWN -> DomainKeyCode.KEYCODE_VOLUME_DOWN
        ProtoKeyCode.KEYCODE_MUTE -> DomainKeyCode.KEYCODE_MUTE
        ProtoKeyCode.KEYCODE_VOLUME_MUTE -> DomainKeyCode.KEYCODE_VOLUME_MUTE

        // Media Controls
        ProtoKeyCode.KEYCODE_MEDIA_PLAY_PAUSE -> DomainKeyCode.KEYCODE_MEDIA_PLAY_PAUSE
        ProtoKeyCode.KEYCODE_MEDIA_STOP -> DomainKeyCode.KEYCODE_MEDIA_STOP
        ProtoKeyCode.KEYCODE_MEDIA_NEXT -> DomainKeyCode.KEYCODE_MEDIA_NEXT
        ProtoKeyCode.KEYCODE_MEDIA_PREVIOUS -> DomainKeyCode.KEYCODE_MEDIA_PREVIOUS
        ProtoKeyCode.KEYCODE_MEDIA_REWIND -> DomainKeyCode.KEYCODE_MEDIA_REWIND
        ProtoKeyCode.KEYCODE_MEDIA_FAST_FORWARD -> DomainKeyCode.KEYCODE_MEDIA_FAST_FORWARD

        // Channel
        ProtoKeyCode.KEYCODE_CHANNEL_UP -> DomainKeyCode.KEYCODE_CHANNEL_UP
        ProtoKeyCode.KEYCODE_CHANNEL_DOWN -> DomainKeyCode.KEYCODE_CHANNEL_DOWN

        // Numbers
        ProtoKeyCode.KEYCODE_0 -> DomainKeyCode.KEYCODE_0
        ProtoKeyCode.KEYCODE_1 -> DomainKeyCode.KEYCODE_1
        ProtoKeyCode.KEYCODE_2 -> DomainKeyCode.KEYCODE_2
        ProtoKeyCode.KEYCODE_3 -> DomainKeyCode.KEYCODE_3
        ProtoKeyCode.KEYCODE_4 -> DomainKeyCode.KEYCODE_4
        ProtoKeyCode.KEYCODE_5 -> DomainKeyCode.KEYCODE_5
        ProtoKeyCode.KEYCODE_6 -> DomainKeyCode.KEYCODE_6
        ProtoKeyCode.KEYCODE_7 -> DomainKeyCode.KEYCODE_7
        ProtoKeyCode.KEYCODE_8 -> DomainKeyCode.KEYCODE_8
        ProtoKeyCode.KEYCODE_9 -> DomainKeyCode.KEYCODE_9

        // Other
        ProtoKeyCode.KEYCODE_GUIDE -> DomainKeyCode.KEYCODE_GUIDE
        ProtoKeyCode.KEYCODE_INFO -> DomainKeyCode.KEYCODE_INFO
        ProtoKeyCode.KEYCODE_CAPTIONS -> DomainKeyCode.KEYCODE_CAPTIONS
        ProtoKeyCode.KEYCODE_SETTINGS -> DomainKeyCode.KEYCODE_SETTINGS
        ProtoKeyCode.KEYCODE_TV -> DomainKeyCode.KEYCODE_TV
        ProtoKeyCode.KEYCODE_TV_INPUT -> DomainKeyCode.KEYCODE_TV_INPUT // Fixed
        ProtoKeyCode.KEYCODE_SLEEP -> DomainKeyCode.KEYCODE_SLEEP
        ProtoKeyCode.KEYCODE_HELP -> DomainKeyCode.KEYCODE_HELP
        ProtoKeyCode.KEYCODE_LANGUAGE_SWITCH -> DomainKeyCode.KEYCODE_LANGUAGE_SWITCH // Fixed
        ProtoKeyCode.KEYCODE_TV_ZOOM_MODE -> DomainKeyCode.KEYCODE_TV_ZOOM_MODE // Fixed
        ProtoKeyCode.KEYCODE_BUTTON_MODE -> DomainKeyCode.KEYCODE_BUTTON_MODE // Fixed
        ProtoKeyCode.KEYCODE_TV_CONTENTS_MENU -> DomainKeyCode.KEYCODE_TV_CONTENTS_MENU // Fixed
        ProtoKeyCode.KEYCODE_MEDIA_AUDIO_TRACK -> DomainKeyCode.KEYCODE_MEDIA_AUDIO_TRACK // Fixed
        ProtoKeyCode.KEYCODE_CAPTIONS -> DomainKeyCode.KEYCODE_CAPTIONS // Fixed
        ProtoKeyCode.KEYCODE_TV_TELETEXT -> DomainKeyCode.KEYCODE_TV_TELETEXT // Fixed
        ProtoKeyCode.KEYCODE_PROG_RED -> DomainKeyCode.KEYCODE_PROG_RED // Fixed
        ProtoKeyCode.KEYCODE_PROG_GREEN -> DomainKeyCode.KEYCODE_PROG_GREEN // Fixed
        ProtoKeyCode.KEYCODE_PROG_YELLOW -> DomainKeyCode.KEYCODE_PROG_YELLOW // Fixed
        ProtoKeyCode.KEYCODE_PROG_BLUE -> DomainKeyCode.KEYCODE_PROG_BLUE // Fixed

        // Handle remaining keys or add else branch
        else -> DomainKeyCode.KEYCODE_UNKNOWN
    }

    fun mapToProto(domainKeyCode: DomainKeyCode): ProtoKeyCode = when (domainKeyCode) {
        // System Keys
        DomainKeyCode.KEYCODE_UNKNOWN -> ProtoKeyCode.KEYCODE_UNKNOWN
        DomainKeyCode.KEYCODE_POWER -> ProtoKeyCode.KEYCODE_POWER
        DomainKeyCode.KEYCODE_HOME -> ProtoKeyCode.KEYCODE_HOME
        DomainKeyCode.KEYCODE_BACK -> ProtoKeyCode.KEYCODE_BACK
        DomainKeyCode.KEYCODE_MENU -> ProtoKeyCode.KEYCODE_MENU
        DomainKeyCode.KEYCODE_SETTINGS -> ProtoKeyCode.KEYCODE_SETTINGS

        // Navigation
        DomainKeyCode.KEYCODE_DPAD_UP -> ProtoKeyCode.KEYCODE_DPAD_UP
        DomainKeyCode.KEYCODE_DPAD_DOWN -> ProtoKeyCode.KEYCODE_DPAD_DOWN
        DomainKeyCode.KEYCODE_DPAD_LEFT -> ProtoKeyCode.KEYCODE_DPAD_LEFT
        DomainKeyCode.KEYCODE_DPAD_RIGHT -> ProtoKeyCode.KEYCODE_DPAD_RIGHT
        DomainKeyCode.KEYCODE_DPAD_CENTER -> ProtoKeyCode.KEYCODE_DPAD_CENTER
        DomainKeyCode.KEYCODE_DPAD_UP_LEFT -> ProtoKeyCode.KEYCODE_DPAD_UP_LEFT
        DomainKeyCode.KEYCODE_DPAD_DOWN_LEFT -> ProtoKeyCode.KEYCODE_DPAD_DOWN_LEFT
        DomainKeyCode.KEYCODE_DPAD_UP_RIGHT -> ProtoKeyCode.KEYCODE_DPAD_UP_RIGHT
        DomainKeyCode.KEYCODE_DPAD_DOWN_RIGHT -> ProtoKeyCode.KEYCODE_DPAD_DOWN_RIGHT

        // Volume
        DomainKeyCode.KEYCODE_VOLUME_UP -> ProtoKeyCode.KEYCODE_VOLUME_UP
        DomainKeyCode.KEYCODE_VOLUME_DOWN -> ProtoKeyCode.KEYCODE_VOLUME_DOWN
        DomainKeyCode.KEYCODE_VOLUME_MUTE -> ProtoKeyCode.KEYCODE_VOLUME_MUTE

        // Media
        DomainKeyCode.KEYCODE_MEDIA_PLAY -> ProtoKeyCode.KEYCODE_MEDIA_PLAY
        DomainKeyCode.KEYCODE_MEDIA_PAUSE -> ProtoKeyCode.KEYCODE_MEDIA_PAUSE
        DomainKeyCode.KEYCODE_MEDIA_PLAY_PAUSE -> ProtoKeyCode.KEYCODE_MEDIA_PLAY_PAUSE
        DomainKeyCode.KEYCODE_MEDIA_STOP -> ProtoKeyCode.KEYCODE_MEDIA_STOP
        DomainKeyCode.KEYCODE_MEDIA_NEXT -> ProtoKeyCode.KEYCODE_MEDIA_NEXT
        DomainKeyCode.KEYCODE_MEDIA_PREVIOUS -> ProtoKeyCode.KEYCODE_MEDIA_PREVIOUS
        DomainKeyCode.KEYCODE_MEDIA_REWIND -> ProtoKeyCode.KEYCODE_MEDIA_REWIND
        DomainKeyCode.KEYCODE_MEDIA_FAST_FORWARD -> ProtoKeyCode.KEYCODE_MEDIA_FAST_FORWARD

        // Channel
        DomainKeyCode.KEYCODE_CHANNEL_UP -> ProtoKeyCode.KEYCODE_CHANNEL_UP
        DomainKeyCode.KEYCODE_CHANNEL_DOWN -> ProtoKeyCode.KEYCODE_CHANNEL_DOWN

        // Numbers
        DomainKeyCode.KEYCODE_0 -> ProtoKeyCode.KEYCODE_0
        DomainKeyCode.KEYCODE_1 -> ProtoKeyCode.KEYCODE_1
        DomainKeyCode.KEYCODE_2 -> ProtoKeyCode.KEYCODE_2
        DomainKeyCode.KEYCODE_3 -> ProtoKeyCode.KEYCODE_3
        DomainKeyCode.KEYCODE_4 -> ProtoKeyCode.KEYCODE_4
        DomainKeyCode.KEYCODE_5 -> ProtoKeyCode.KEYCODE_5
        DomainKeyCode.KEYCODE_6 -> ProtoKeyCode.KEYCODE_6
        DomainKeyCode.KEYCODE_7 -> ProtoKeyCode.KEYCODE_7
        DomainKeyCode.KEYCODE_8 -> ProtoKeyCode.KEYCODE_8
        DomainKeyCode.KEYCODE_9 -> ProtoKeyCode.KEYCODE_9

        // Other
        DomainKeyCode.KEYCODE_GUIDE -> ProtoKeyCode.KEYCODE_GUIDE 
        DomainKeyCode.KEYCODE_INFO -> ProtoKeyCode.KEYCODE_INFO
        DomainKeyCode.KEYCODE_CAPTIONS -> ProtoKeyCode.KEYCODE_CAPTIONS
        DomainKeyCode.KEYCODE_SETTINGS -> ProtoKeyCode.KEYCODE_SETTINGS
        DomainKeyCode.KEYCODE_TV -> ProtoKeyCode.KEYCODE_TV
        DomainKeyCode.KEYCODE_TV_INPUT -> ProtoKeyCode.KEYCODE_TV_INPUT // Fixed
        DomainKeyCode.KEYCODE_SLEEP -> ProtoKeyCode.KEYCODE_SLEEP
        DomainKeyCode.KEYCODE_HELP -> ProtoKeyCode.KEYCODE_HELP
        DomainKeyCode.KEYCODE_LANGUAGE_SWITCH -> ProtoKeyCode.KEYCODE_LANGUAGE_SWITCH // Fixed
        DomainKeyCode.KEYCODE_TV_ZOOM_MODE -> ProtoKeyCode.KEYCODE_TV_ZOOM_MODE // Fixed
        DomainKeyCode.KEYCODE_BUTTON_MODE -> ProtoKeyCode.KEYCODE_BUTTON_MODE // Fixed
        DomainKeyCode.KEYCODE_TV_CONTENTS_MENU -> ProtoKeyCode.KEYCODE_TV_CONTENTS_MENU // Fixed
        DomainKeyCode.KEYCODE_MEDIA_AUDIO_TRACK -> ProtoKeyCode.KEYCODE_MEDIA_AUDIO_TRACK // Fixed
        DomainKeyCode.KEYCODE_CAPTIONS -> ProtoKeyCode.KEYCODE_CAPTIONS // Fixed
        DomainKeyCode.KEYCODE_TV_TELETEXT -> ProtoKeyCode.KEYCODE_TV_TELETEXT // Fixed
        DomainKeyCode.KEYCODE_RED -> ProtoKeyCode.KEYCODE_PROG_RED // Fixed
        DomainKeyCode.KEYCODE_GREEN -> ProtoKeyCode.KEYCODE_PROG_GREEN // Fixed
        DomainKeyCode.KEYCODE_YELLOW -> ProtoKeyCode.KEYCODE_PROG_YELLOW // Fixed
        DomainKeyCode.KEYCODE_BLUE -> ProtoKeyCode.KEYCODE_PROG_BLUE // Fixed

        // Handle remaining keys or add else branch
        else -> ProtoKeyCode.KEYCODE_UNKNOWN
    }
}
