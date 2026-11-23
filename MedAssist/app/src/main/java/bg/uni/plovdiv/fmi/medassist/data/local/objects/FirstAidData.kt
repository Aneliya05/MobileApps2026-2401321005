package bg.uni.plovdiv.fmi.medassist.data.local.objects

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import bg.uni.plovdiv.fmi.medassist.data.local.entity.FirstAidTopicEntity

object FirstAidData {
    val topics = listOf(
        FirstAidTopicEntity(
            id = 1,
            title = "Severe Bleeding",
            subtitle = "How to stop heavy blood loss safely",
            category = "Critical",
            icon = Icons.Default.Bloodtype,
            steps = listOf(
                "Call 112 immediately if bleeding is severe or won't stop.",
                "Put on disposable gloves if available to prevent infection.",
                "Apply firm, direct pressure on the wound using a clean cloth or sterile bandage.",
                "If blood soaks through, place another bandage on top without removing the first one.",
                "Keep the injured limb elevated above the level of the heart if possible."
            ),
            doNotDo = listOf(
                "Do NOT remove embedded objects (glass, metal) from the wound.",
                "Do NOT apply a tourniquet unless trained and bleeding is life-threatening."
            )
        ),
        FirstAidTopicEntity(
            id = 2,
            title = "Thermal Burns",
            subtitle = "First aid for minor and major burns",
            category = "Injuries",
            icon = Icons.Default.LocalFireDepartment,
            steps = listOf(
                "Cool the burn immediately under cool (not ice-cold) running water for at least 10-20 minutes.",
                "Gently remove rings, watches, or tight clothing near the area before swelling starts.",
                "Cover the burn loosely with sterile, non-stick gauze or clean plastic wrap.",
                "Take over-the-counter pain relief if needed and seek medical help for large burns."
            ),
            doNotDo = listOf(
                "Do NOT apply ice, butter, oil, toothpaste, or ointments to a fresh burn.",
                "Do NOT break blisters, as this increases the risk of infection."
            )
        ),
        FirstAidTopicEntity(
            id = 3,
            title = "CPR (Cardiac Arrest)",
            subtitle = "Life-saving chest compressions",
            category = "Critical",
            icon = Icons.Default.Favorite,
            steps = listOf(
                "Check if the scene is safe and tap the person loudly asking 'Are you okay?'",
                "If unresponsive and not breathing normally, call 112 immediately and put on speaker.",
                "Place the heel of one hand in the center of the chest, put your other hand on top.",
                "Push hard and fast (5-6 cm deep) at a rate of 100-120 compressions per minute.",
                "Continue without stopping until paramedics arrive or an AED is ready."
            ),
            doNotDo = listOf(
                "Do NOT stop compressions for more than 10 seconds.",
                "Do NOT perform CPR if the person is breathing normally."
            )
        )

        // TODO: ТВОЯ КОД ТУК!
        // Добави още 2 или 3 ситуации (напр. "Choking", "Fainting", "Fractures") по горния шаблон!
    )
}