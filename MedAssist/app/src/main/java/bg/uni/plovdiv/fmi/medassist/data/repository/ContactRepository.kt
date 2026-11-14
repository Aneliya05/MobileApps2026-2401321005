package bg.uni.plovdiv.fmi.medassist.data.repository

import bg.uni.plovdiv.fmi.medassist.data.local.dao.ContactDao
import bg.uni.plovdiv.fmi.medassist.data.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {
    val allContacts: Flow<List<ContactEntity>> = contactDao.getAllContacts()

    suspend fun insert(contact: ContactEntity) = contactDao.insertContact(contact)
    suspend fun update(contact: ContactEntity) = contactDao.updateContact(contact)
    suspend fun delete(contact: ContactEntity) = contactDao.deleteContact(contact)
}