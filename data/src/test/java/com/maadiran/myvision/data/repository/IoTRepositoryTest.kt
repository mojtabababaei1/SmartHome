package com.maadiran.myvision.data.repository

import com.maadiran.myvision.data.local.PreferencesManager
import com.maadiran.myvision.data.network.DynamicBaseUrlInterceptor
import com.maadiran.myvision.data.network.IoTApiService
import com.maadiran.myvision.data.network.models.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class IoTRepositoryTest {
    private lateinit var repository: IoTRepository
    private val apiService: IoTApiService = mockk()
    private val dynamicBaseUrlInterceptor: DynamicBaseUrlInterceptor = mockk(relaxed = true)
    private val preferencesManager: PreferencesManager = mockk(relaxed = true)
    
    private val testDeviceType = "fridge"
    private val testIpAddress = "192.168.1.100"

    @BeforeEach
    fun setup() {
        repository = IoTRepository(apiService, dynamicBaseUrlInterceptor, preferencesManager)
        coEvery { preferencesManager.getIpAddress(any()) } returns testIpAddress
    }

    @Test
    fun `configureWifi should save IP and return success`() = runTest {
        // Given
        val ssid = "TestWiFi"
        val password = "password123"
        val expectedIp = "192.168.1.100"
        
        coEvery { apiService.configureWifi(ssid, password) } returns 
            Response.success(WifiConnectResponse(status = "success", ssid = ssid, ip = expectedIp))
        coEvery { preferencesManager.saveIpAddress(any(), any()) } just Runs

        // When
        val result = repository.configureWifi(testDeviceType, ssid, password)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedIp, result.getOrNull())
        coVerify { preferencesManager.saveIpAddress(testDeviceType, expectedIp) }
        verify { dynamicBaseUrlInterceptor.updateBaseUrl("192.168.4.1") }
    }

    @Test
    fun `getTemperatures should return temperature data`() = runTest {
        // Given
        val mockResponse = TemperatureData(
            status = "success",
            FridgeTemp = 4.0f,
            FreezeTemp = -18.0f
        )
        coEvery { apiService.getTemperatures() } returns Response.success(mockResponse)

        // When
        val result = repository.getTemperatures(testDeviceType)

        // Then
        assertTrue(result.isSuccess)
        result.onSuccess { data ->
            assertEquals(4.0f, data.fridgeTemp)
            assertEquals(-18.0f, data.freezerTemp)
        }
        verify { dynamicBaseUrlInterceptor.updateBaseUrl(testIpAddress) }
    }

    @Test
    fun `getDoorStatuses should return door status`() = runTest {
        // Given
        val mockResponse = DoorStatusResponse(
            status = "success",
            fridgeDoor = "closed",
            freezerDoor = "closed"
        )
        coEvery { apiService.getDoorStatuses() } returns Response.success(mockResponse)

        // When
        val result = repository.getDoorStatuses(testDeviceType)

        // Then
        assertTrue(result.isSuccess)
        result.onSuccess { status ->
            assertEquals("closed", status.fridgeDoor)
            assertEquals("closed", status.freezerDoor)
        }
        verify { dynamicBaseUrlInterceptor.updateBaseUrl(testIpAddress) }
    }

    @Test
    fun `sendCommand should return success`() = runTest {
        // Given
        val command = "test_command"
        coEvery { apiService.sendCommand(command) } returns 
            Response.success(CommandResult(status = "success"))

        // When
        val result = repository.sendCommand(testDeviceType, command)

        // Then
        assertTrue(result.isSuccess)
        verify { dynamicBaseUrlInterceptor.updateBaseUrl(testIpAddress) }
    }

    @Test
    fun `operations should fail when no IP address is found`() = runTest {
        // Given
        coEvery { preferencesManager.getIpAddress(any()) } returns null

        // When/Then
        suspend {
            assertTrue(repository.getTemperatures(testDeviceType).isFailure)
            assertTrue(repository.getDoorStatuses(testDeviceType).isFailure)
            assertTrue(repository.sendCommand(testDeviceType, "command").isFailure)
        }
    }
}