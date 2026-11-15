package bg.uni.plovdiv.fmi.medassist.data.local.entity

import androidx.activity.SystemBarStyle.Companion.auto
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lab_tests")
data class LabTestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val testName: String,
    val date: String,
    val extractedText: String,
    val hasAbnormalities: Boolean
);
