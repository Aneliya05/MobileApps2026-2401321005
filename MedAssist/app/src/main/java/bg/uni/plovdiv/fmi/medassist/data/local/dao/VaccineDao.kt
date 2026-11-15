package bg.uni.plovdiv.fmi.medassist.data.local.dao

import androidx.room.*
import bg.uni.plovdiv.fmi.medassist.data.local.entity.VaccineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccineDao {
    @Query("SELECT * FROM vaccines ORDER BY id DESC")
    fun getAllVaccines(): Flow<List<VaccineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccine(labTest: VaccineEntity)

    @Delete
    suspend fun deleteVaccine(labTest: VaccineEntity)

}