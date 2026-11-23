package bg.uni.plovdiv.fmi.medassist.data.local.entity

import androidx.compose.ui.graphics.vector.ImageVector

data class FirstAidTopicEntity(
    val id: Int,
    val title: String,
    val subtitle: String,
    val category: String,
    val icon: ImageVector,
    val steps: List<String>,
    val doNotDo: List<String>
)

