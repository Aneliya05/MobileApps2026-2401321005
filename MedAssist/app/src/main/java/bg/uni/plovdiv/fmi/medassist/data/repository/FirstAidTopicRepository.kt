package bg.uni.plovdiv.fmi.medassist.data.repository

import bg.uni.plovdiv.fmi.medassist.data.local.dao.FirstAidTopicDao
import bg.uni.plovdiv.fmi.medassist.data.local.entity.FirstAidTopicEntity
import kotlinx.coroutines.flow.Flow

class FirstAidTopicRepository(private val firstAidTopicDao: FirstAidTopicDao) {
    val allFirstAidTopics: Flow<List<FirstAidTopicEntity>> = firstAidTopicDao.getAllFirstAidTopics()

    suspend fun insert(topic: FirstAidTopicEntity) = firstAidTopicDao.insertFirstAidTopicEntity(topic)
    suspend fun update(topic: FirstAidTopicEntity) = firstAidTopicDao.updateFirstAidTopicEntity(topic)
    suspend fun delete(topic: FirstAidTopicEntity) = firstAidTopicDao.deleteFirstAidTopicEntity(topic)
}