package bg.uni.plovdiv.fmi.medassist.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class NominatimPlaceDto(
    @SerializedName("place_id") val placeId: Long,
    @SerializedName("display_name") val displayName: String,
    val name: String?,
    val lat: String,
    val lon: String
)

interface NominatimApiService {
    @GET("search")
    suspend fun searchHospitals(
        @Header("User-Agent") userAgent: String = "MedAssistUniversityProject/1.0",
        @Query("q") query: String = "hospital",
        @Query("format") format: String = "json",
        @Query("viewbox") viewbox: String, // minLon,maxLat,maxLon,minLat
        @Query("bounded") bounded: Int = 1,
        @Query("limit") limit: Int = 15
    ): List<NominatimPlaceDto>

    companion object {
        private const val BASE_URL = "https://nominatim.openstreetmap.org/"

        fun create(): NominatimApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NominatimApiService::class.java)
        }
    }
}