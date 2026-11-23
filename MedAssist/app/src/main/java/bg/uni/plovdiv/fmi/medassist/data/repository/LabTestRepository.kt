package bg.uni.plovdiv.fmi.medassist.data.repository

import bg.uni.plovdiv.fmi.medassist.data.local.dao.LabTestDao
import bg.uni.plovdiv.fmi.medassist.data.local.entity.LabTestEntity
import kotlinx.coroutines.flow.Flow

class LabTestRepository(private val labTestDao: LabTestDao) {
    val allLabTests: Flow<List<LabTestEntity>> = labTestDao.getAllLabTests()

    suspend fun insert(labTest: LabTestEntity) = labTestDao.insertLabTest(labTest)
    suspend fun update(labTest: LabTestEntity) = labTestDao.updateLabTest(labTest)
    suspend fun delete(labTest: LabTestEntity) = labTestDao.deleteLabTest(labTest)
}