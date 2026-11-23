package bg.uni.plovdiv.fmi.medassist.presentation.guide

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bg.uni.plovdiv.fmi.medassist.data.local.entity.FirstAidTopicEntity
import bg.uni.plovdiv.fmi.medassist.data.remote.FirstAidApiService
import bg.uni.plovdiv.fmi.medassist.data.local.objects.FirstAidData
import bg.uni.plovdiv.fmi.medassist.data.repository.GuideRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GuideViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GuideRepository = GuideRepository(FirstAidApiService.create())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTopic = MutableStateFlow<FirstAidTopicEntity?>(null)
    val selectedTopic: StateFlow<FirstAidTopicEntity?> = _selectedTopic.asStateFlow()

    val filteredTopics: StateFlow<List<FirstAidTopicEntity>> = combine(
        repository.topicsFlow,
        _searchQuery
    ) { topics, query ->
        if (query.isBlank()) {
            topics
        } else {
            topics.filter { topic ->
                topic.title.contains(query, ignoreCase = true) ||
                        topic.subtitle.contains(query, ignoreCase = true) ||
                        topic.category.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FirstAidData.topics)

    init {
        viewModelScope.launch {
            repository.refreshTopics()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun selectTopic(topic: FirstAidTopicEntity?) {
        _selectedTopic.value = topic
    }
}
