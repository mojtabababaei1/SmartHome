package com.maadiran.myvision.presentation.features.devices.tv.ui.models

enum class ControlMode {
    DPAD {
        override val displayName = "D-Pad"
    },
    TOUCHPAD {
        override val displayName = "Touch"
    },
    KEYPAD {
        override val displayName = "Keypad"
    },
    KEYBOARD {
        override val displayName = "Keyboard"
    };

    abstract val displayName: String

    companion object {
        fun fromString(value: String): ControlMode =
            values().find { it.name.equals(value, ignoreCase = true) } ?: DPAD
    }
}