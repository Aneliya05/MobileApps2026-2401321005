package bg.uni.plovdiv.fmi.medassist.data.local.dao

import androidx.room.*
import bg.uni.plovdiv.fmi.medassist.data.local.entity.LabTestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabTestDao {
    @Query("SELECT * FROM lab_tests ORDER BY id DESC")
    fun getAllLabTests(): Flow<List<LabTestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabTest(labTest: LabTestEntity)

    @Delete
    suspend fun deleteLabTest(labTest: LabTestEntity)

}