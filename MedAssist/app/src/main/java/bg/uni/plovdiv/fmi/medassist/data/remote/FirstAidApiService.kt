package bg.uni.plovdiv.fmi.medassist.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class FirstAidTopicDto(
    val id: Int,
    val title: String,
    val subtitle: String,
    val category: String,
    val iconName: String,
    val steps: List<String>,
    val doNotDo: List<String>
)

interface FirstAidApiService {
    @GET("Aneliya05/9587acb83dfe7426baf8bab489fa7259/raw")
    suspend fun getFirstAidTopics(): List<FirstAidTopicDto>

    companion object {
        private const val BASE_URL = "https://gist.githubusercontent.com/"

        fun create(): FirstAidApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FirstAidApiService::class.java)
        }
    }
}