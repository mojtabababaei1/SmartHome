package com.maadiran.myvision.data.models

import com.google.gson.annotations.SerializedName

data class DoorStatusDto(
    @SerializedName("FridgeDoor") 
    val fridgeDoor: String,
    @SerializedName("FreezeDoor") 
    val freezerDoor: String
)