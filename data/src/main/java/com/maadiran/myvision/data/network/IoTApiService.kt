package com.maadiran.myvision.data.network

import com.maadiran.myvision.data.network.models.CommandResult
import com.maadiran.myvision.data.network.models.DoorStatusResponse
import com.maadiran.myvision.data.network.models.TemperatureData
import com.maadiran.myvision.data.network.models.WifiConnectResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IoTApiService {
    @POST("api/{command}")
    suspend fun sendCommand(@Path("command") command: String): Response<CommandResult>

    @GET("api/MonitorTemps")
    suspend fun getTemperatures(): Response<TemperatureData>

    @GET("api/MonitorDoors")
    suspend fun getDoorStatuses(): Response<DoorStatusResponse>

    @FormUrlEncoded
    @POST("connect")
    suspend fun configureWifi(
        @Field("ssid") ssid: String,
        @Field("password") password: String
    ): Response<WifiConnectResponse>
}