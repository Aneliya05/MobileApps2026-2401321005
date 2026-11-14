package bg.uni.plovdiv.fmi.medassist.presentation.contacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bg.uni.plovdiv.fmi.medassist.data.local.database.AppDatabase
import bg.uni.plovdiv.fmi.medassist.data.local.entity.ContactEntity
import bg.uni.plovdiv.fmi.medassist.data.repository.ContactRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ContactRepository

    val contacts: StateFlow<List<ContactEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).contactDao()
        repository = ContactRepository(dao)
        contacts = repository.allContacts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun addContact(name: String, phone: String, relation: String) {
        viewModelScope.launch {
            repository.insert(
                ContactEntity(
                    name = name.trim(),
                    phoneNumber = phone.trim(),
                    relation = relation.trim()
                )
            )
        }
    }

    fun updateContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.update(contact)
        }
    }

    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }
}