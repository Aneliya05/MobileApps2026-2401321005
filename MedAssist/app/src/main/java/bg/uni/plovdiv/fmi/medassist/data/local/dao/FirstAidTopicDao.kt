package bg.uni.plovdiv.fmi.medassist.data.local.dao

import androidx.room.*
import bg.uni.plovdiv.fmi.medassist.data.local.entity.FirstAidTopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FirstAidTopicDao {
    @Query("SELECT * FROM first_aid_topics ORDER BY id DESC")
    fun getAllFirstAidTopics(): Flow<List<FirstAidTopicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFirstAidTopicEntity(FirstAidTopicEntity: FirstAidTopicEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFirstAidTopicEntity(FirstAidTopicEntity: FirstAidTopicEntity)

    @Delete
    suspend fun deleteFirstAidTopicEntity(FirstAidTopicEntity: FirstAidTopicEntity)

}