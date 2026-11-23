package bg.uni.plovdiv.fmi.medassist.data.repository

import bg.uni.plovdiv.fmi.medassist.data.local.dao.VaccineDao
import bg.uni.plovdiv.fmi.medassist.data.local.entity.VaccineEntity
import kotlinx.coroutines.flow.Flow

class VaccineRepository(private val vaccineDao: VaccineDao) {
    val allVaccines: Flow<List<VaccineEntity>> = vaccineDao.getAllVaccines()

    suspend fun insert(vaccine: VaccineEntity) = vaccineDao.insertVaccine(vaccine)
    suspend fun update(vaccine: VaccineEntity) = vaccineDao.updateVaccine(vaccine)
    suspend fun delete(vaccine: VaccineEntity) = vaccineDao.deleteVaccine(vaccine)
}
