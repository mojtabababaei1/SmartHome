package com.maadiran.myvision.domain.model

enum class RemoteKeyCode {
    // System Keys
    KEYCODE_UNKNOWN,
    KEYCODE_POWER,
    KEYCODE_HOME,
    KEYCODE_BACK,
    KEYCODE_MENU,
    KEYCODE_SETTINGS,
    
    // Navigation
    KEYCODE_DPAD_UP,
    KEYCODE_DPAD_DOWN,
    KEYCODE_DPAD_LEFT,
    KEYCODE_DPAD_RIGHT,
    KEYCODE_DPAD_CENTER,
    KEYCODE_DPAD_UP_LEFT,
    KEYCODE_DPAD_DOWN_LEFT,
    KEYCODE_DPAD_UP_RIGHT,
    KEYCODE_DPAD_DOWN_RIGHT,
    
    // Volume Controls
    KEYCODE_VOLUME_UP,
    KEYCODE_VOLUME_DOWN,
    KEYCODE_MUTE,
    KEYCODE_VOLUME_MUTE,
    
    // Media Controls
    KEYCODE_MEDIA_PLAY_PAUSE,
    KEYCODE_MEDIA_STOP,
    KEYCODE_MEDIA_NEXT,
    KEYCODE_MEDIA_PREVIOUS,
    KEYCODE_MEDIA_REWIND,
    KEYCODE_MEDIA_FAST_FORWARD,
    KEYCODE_MEDIA_PLAY,
    KEYCODE_MEDIA_PAUSE,
    KEYCODE_MEDIA_CLOSE,
    KEYCODE_MEDIA_EJECT,
    KEYCODE_MEDIA_RECORD,
    KEYCODE_MEDIA_SKIP_FORWARD,
    KEYCODE_MEDIA_SKIP_BACKWARD,
    KEYCODE_MEDIA_STEP_FORWARD,
    KEYCODE_MEDIA_STEP_BACKWARD,
    
    // TV Specific
    KEYCODE_TV,
    KEYCODE_TV_POWER,
    KEYCODE_TV_INPUT,  // Instead of KEYCODE_INPUT
    KEYCODE_TV_INPUT_HDMI_1,
    KEYCODE_TV_INPUT_HDMI_2,
    KEYCODE_TV_INPUT_HDMI_3,
    KEYCODE_TV_INPUT_HDMI_4,
    KEYCODE_TV_INPUT_COMPOSITE_1,
    KEYCODE_TV_INPUT_COMPOSITE_2,
    KEYCODE_TV_INPUT_COMPONENT_1,
    KEYCODE_TV_INPUT_COMPONENT_2,
    KEYCODE_TV_INPUT_VGA_1,
    KEYCODE_TV_DATA_SERVICE,
    KEYCODE_TV_RADIO_SERVICE,
    KEYCODE_TV_TELETEXT,      // Instead of KEYCODE_TELETEXT
    KEYCODE_TV_NUMBER_ENTRY,
    KEYCODE_TV_TERRESTRIAL_ANALOG,
    KEYCODE_TV_TERRESTRIAL_DIGITAL,
    KEYCODE_TV_SATELLITE,
    KEYCODE_TV_SATELLITE_BS,
    KEYCODE_TV_SATELLITE_CS,
    KEYCODE_TV_SATELLITE_SERVICE,
    KEYCODE_TV_NETWORK,
    KEYCODE_TV_ANTENNA_CABLE,
    KEYCODE_TV_AUDIO_DESCRIPTION,
    KEYCODE_TV_AUDIO_DESCRIPTION_MIX_UP,
    KEYCODE_TV_AUDIO_DESCRIPTION_MIX_DOWN,
    KEYCODE_TV_ZOOM_MODE,     // Instead of KEYCODE_ZOOM
    KEYCODE_TV_CONTENTS_MENU, // Instead of KEYCODE_ASPECT_RATIO
    KEYCODE_TV_MEDIA_CONTEXT_MENU,
    KEYCODE_TV_TIMER_PROGRAMMING,
    KEYCODE_CHANNEL_UP,
    KEYCODE_CHANNEL_DOWN,
    KEYCODE_GUIDE,
    KEYCODE_INFO,
    KEYCODE_CAPTIONS,         // Instead of KEYCODE_SUBTITLE
    
    // Numbers
    KEYCODE_0,
    KEYCODE_1,
    KEYCODE_2,
    KEYCODE_3,
    KEYCODE_4,
    KEYCODE_5,
    KEYCODE_6,
    KEYCODE_7,
    KEYCODE_8,
    KEYCODE_9,
    KEYCODE_11,
    KEYCODE_12,
    
    // Text Input
    KEYCODE_SPACE,
    KEYCODE_ENTER,
    KEYCODE_DEL,
    KEYCODE_A,
    KEYCODE_B,
    KEYCODE_C,
    KEYCODE_D,
    KEYCODE_E,
    KEYCODE_F,
    KEYCODE_G,
    KEYCODE_H,
    KEYCODE_I,
    KEYCODE_J,
    KEYCODE_K,
    KEYCODE_L,
    KEYCODE_M,
    KEYCODE_N,
    KEYCODE_O,
    KEYCODE_P,
    KEYCODE_Q,
    KEYCODE_R,
    KEYCODE_S,
    KEYCODE_T,
    KEYCODE_U,
    KEYCODE_V,
    KEYCODE_W,
    KEYCODE_X,
    KEYCODE_Y,
    KEYCODE_Z,
    
    // Smart TV Features
    KEYCODE_RED,
    KEYCODE_GREEN,
    KEYCODE_YELLOW,
    KEYCODE_BLUE,
    KEYCODE_VOICE_ASSIST,
    
    // App Navigation
    KEYCODE_APP_SWITCH,
    KEYCODE_NAVIGATE_PREVIOUS,
    KEYCODE_NAVIGATE_NEXT,
    KEYCODE_NAVIGATE_IN,
    KEYCODE_NAVIGATE_OUT,
    
    // Media Apps
    KEYCODE_VIDEO_APP_1, // Netflix
    KEYCODE_VIDEO_APP_2, // YouTube
    KEYCODE_VIDEO_APP_3, // Prime Video
    KEYCODE_VIDEO_APP_4, // Disney+
    KEYCODE_VIDEO_APP_5,
    KEYCODE_VIDEO_APP_6,
    KEYCODE_VIDEO_APP_7,
    KEYCODE_VIDEO_APP_8,

    // Additional Controls
    KEYCODE_HELP,
    KEYCODE_SLEEP,
    KEYCODE_LANGUAGE_SWITCH,  // Instead of KEYCODE_LANGUAGE
    KEYCODE_BUTTON_MODE,          // Instead of KEYCODE_PICTURE_MODE
    KEYCODE_MEDIA_AUDIO_TRACK,    // Instead of KEYCODE_AUDIO_MODE
    KEYCODE_PROG_RED,            // For red button
    KEYCODE_PROG_GREEN,          // For green button
    KEYCODE_PROG_YELLOW,         // For yellow button
    KEYCODE_PROG_BLUE,           // For blue button

    // Other Controls (DVR, Input switching etc)
    KEYCODE_DVR,
    KEYCODE_BOOKMARK,
    KEYCODE_STB_POWER,
    KEYCODE_STB_INPUT,
    KEYCODE_AVR_POWER,
    KEYCODE_AVR_INPUT,
    KEYCODE_WINDOW,
    KEYCODE_PAIRING,
    KEYCODE_MEDIA_TOP_MENU,
    KEYCODE_LAST_CHANNEL,
    KEYCODE_ALL_APPS
}
