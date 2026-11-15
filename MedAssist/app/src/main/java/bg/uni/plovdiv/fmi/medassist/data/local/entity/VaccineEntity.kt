package bg.uni.plovdiv.fmi.medassist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaccines")
data class VaccineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vaccineName: String,
    val dateAdministered: String,
    val nextDoseDate: String = ""
)