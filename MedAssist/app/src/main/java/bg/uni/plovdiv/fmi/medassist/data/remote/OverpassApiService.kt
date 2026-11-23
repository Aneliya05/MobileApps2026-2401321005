package bg.uni.plovdiv.fmi.medassist.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class OverpassResponse(
    val elements: List<OverpassElement> = emptyList()
)

data class OverpassElement(
    val id: Long,
    val lat: Double?,
    val lon: Double?,
    val center: OverpassCenter?,
    val tags: Map<String, String>?
)

data class OverpassCenter(
    val lat: Double,
    val lon: Double
)

interface OverpassApiService {
    @GET("api/interpreter")
    suspend fun getNearbyHospitals(@Query("data") query: String): OverpassResponse

    companion object {
        private val urls = listOf(
            "https://overpass-api.de/",
            "https://lz4.overpass-api.de/",
            "https://kumi.systems/"
        )

        fun create(baseUrl: String = urls[0]): OverpassApiService {
            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OverpassApiService::class.java)
        }

        // Автоматично опитва различни огледални сървъри (mirrors) на Overpass API при грешка или rate-limit
        suspend fun fetchHospitalsWithFallbacks(query: String): OverpassResponse {
            var lastException: Exception? = null
            for (url in urls) {
                try {
                    val service = create(url)
                    val result = service.getNearbyHospitals(query)
                    return result
                } catch (e: Exception) {
                    lastException = e
                }
            }
            throw lastException ?: Exception("All Overpass API mirrors failed.")
        }

        // Разширена заявка с по-голям радиус (10 км) и повече видове медицински обекти (болници, клиники, лекари)
        fun buildQuery(lat: Double, lng: Double, radiusMeters: Int = 10000): String {
            return "[out:json][timeout:15];(node[\"amenity\"=\"hospital\"](around:$radiusMeters,$lat,$lng);way[\"amenity\"=\"hospital\"](around:$radiusMeters,$lat,$lng);node[\"amenity\"=\"clinic\"](around:$radiusMeters,$lat,$lng);way[\"amenity\"=\"clinic\"](around:$radiusMeters,$lat,$lng);node[\"healthcare\"=\"hospital\"](around:$radiusMeters,$lat,$lng);way[\"healthcare\"=\"hospital\"](around:$radiusMeters,$lat,$lng);node[\"amenity\"=\"doctors\"](around:$radiusMeters,$lat,$lng););out center 30;"
        }
    }
}
