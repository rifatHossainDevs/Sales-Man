package com.wevx.dealershipmanagement.data.remote.gps

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class LiveGpsRequest(
    val latitude: Double,
    val longitude: Double,
    val placeName: String? = "In Market"
)

data class OfflineRoutePointDto(
    val latitude: Double,
    val longitude: Double,
    val placeName: String? = "Offline Point",
    val recordedAt: String
)

data class SyncOfflineRoutesRequest(
    val points: List<OfflineRoutePointDto>
)

data class GpsApiResponse(
    val statusCode: Int,
    val success: Boolean,
    val message: String
)

data class SalesSummaryRoutePointDto(
    @SerializedName("_id")
    val id: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val placeName: String? = null,
    val recordedAt: String? = null
)

data class SalesSummaryDataDto(
    val date: String? = null,
    val points: List<SalesSummaryRoutePointDto>? = null,
    val totalOrders: Int? = null,
    val totalCollection: Double? = null
)

data class ResponseSalesSummaryDTO(
    val statusCode: Int? = null,
    val success: Boolean? = null,
    val message: String? = null,
    val data: SalesSummaryDataDto? = null
)

interface GpsApiService {
    @POST("users/gps/live-update")
    suspend fun updateLiveLocation(
        @Body request: LiveGpsRequest
    ): Response<GpsApiResponse>

    @POST("users/gps/offline")
    suspend fun markSalesmanOffline(): Response<GpsApiResponse>

    @POST("users/gps/sync-offline-routes")
    suspend fun syncOfflineRoutes(
        @Body request: SyncOfflineRoutesRequest
    ): Response<GpsApiResponse>

    @GET("users/gps/route-history/{salesmanId}")
    suspend fun getSalesmanRouteHistory(
        @Path("salesmanId") salesmanId: String,
        @Query("date") date: String
    ): Response<ResponseSalesSummaryDTO>
}
