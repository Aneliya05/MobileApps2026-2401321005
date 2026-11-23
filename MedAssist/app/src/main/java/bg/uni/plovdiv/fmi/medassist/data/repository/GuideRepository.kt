package bg.uni.plovdiv.fmi.medassist.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import bg.uni.plovdiv.fmi.medassist.data.local.entity.FirstAidTopicEntity
import bg.uni.plovdiv.fmi.medassist.data.local.objects.FirstAidData
import bg.uni.plovdiv.fmi.medassist.data.remote.FirstAidApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GuideRepository(
    private val apiService: FirstAidApiService
) {
    private val _topicsFlow = MutableStateFlow<List<FirstAidTopicEntity>>(FirstAidData.topics)
    val topicsFlow: Flow<List<FirstAidTopicEntity>> = _topicsFlow.asStateFlow()

    suspend fun refreshTopics() {
        try {
            val remoteDtos = apiService.getFirstAidTopics()
            val mappedTopics = remoteDtos.map { dto ->
                FirstAidTopicEntity(
                    id = dto.id,
                    title = dto.title,
                    subtitle = dto.subtitle,
                    category = dto.category,
                    icon = mapIconName(dto.iconName),
                    steps = dto.steps,
                    doNotDo = dto.doNotDo
                )
            }
            if (mappedTopics.isNotEmpty()) {
                _topicsFlow.value = mappedTopics
            }
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("GuideRepository", "Error fetching topics from Gist: ${e.javaClass.simpleName} - ${e.message}", e)
            if (_topicsFlow.value.isEmpty()) {
                _topicsFlow.value = FirstAidData.topics
            }
        }
    }

    private fun mapIconName(name: String): ImageVector {
        return when (name.lowercase()) {
            "bleeding" -> Icons.Default.Bloodtype
            "burn" -> Icons.Default.LocalFireDepartment
            "heart" -> Icons.Default.Favorite
            "choking" -> Icons.Default.Air
            "bone", "fracture" -> Icons.Default.Healing
            "allergy" -> Icons.Default.Warning
            "brain" -> Icons.Default.Psychology
            "fainting" -> Icons.Default.AirlineSeatFlat
            "sun" -> Icons.Default.WbSunny
            else -> Icons.Default.MedicalServices
        }
    }
}
