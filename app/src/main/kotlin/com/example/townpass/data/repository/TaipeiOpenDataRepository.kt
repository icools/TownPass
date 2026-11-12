package com.example.townpass.data.repository

import com.example.townpass.data.service.TaipeiOpenDataService
import com.example.townpass.ui.a2ui.A2UICardData
import com.example.townpass.ui.a2ui.A2UICategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface TaipeiOpenDataRepository {
    val allCards: StateFlow<List<A2UICardData>>
    val filteredCards: StateFlow<List<A2UICardData>>
    val selectedCategory: StateFlow<A2UICategory>
    val searchQuery: StateFlow<String>
    val isAutoRefreshEnabled: StateFlow<Boolean>
    val lastRefreshedTime: StateFlow<String>
    val isRefreshing: StateFlow<Boolean>

    fun setCategory(category: A2UICategory)
    fun setSearchQuery(query: String)
    fun toggleAutoRefresh()
    fun refresh()
    fun getCardById(id: String): A2UICardData?
    fun startTicker()
    fun stopTicker()
}

class TaipeiOpenDataRepositoryImpl(
    private val service: TaipeiOpenDataService = TaipeiOpenDataService(),
    private val externalScope: CoroutineScope = CoroutineScope(kotlinx.coroutines.SupervisorJob() + Dispatchers.Default),
    private val autoStartTicker: Boolean = true
) : TaipeiOpenDataRepository {

    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.TAIWAN)
    private var cycleCount = 0
    private var tickerJob: Job? = null

    private val _allCards = MutableStateFlow<List<A2UICardData>>(emptyList())
    override val allCards: StateFlow<List<A2UICardData>> = _allCards.asStateFlow()

    private val _selectedCategory = MutableStateFlow(A2UICategory.ALL)
    override val selectedCategory: StateFlow<A2UICategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    override val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isAutoRefreshEnabled = MutableStateFlow(true)
    override val isAutoRefreshEnabled: StateFlow<Boolean> = _isAutoRefreshEnabled.asStateFlow()

    private val _lastRefreshedTime = MutableStateFlow(timeFormat.format(Date()))
    override val lastRefreshedTime: StateFlow<String> = _lastRefreshedTime.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    override val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    override val filteredCards: StateFlow<List<A2UICardData>> = combine(
        _allCards,
        _selectedCategory,
        _searchQuery
    ) { cards, category, query ->
        cards.filter { card ->
            val matchCategory = category == A2UICategory.ALL || card.category == category
            val matchQuery = query.isBlank() ||
                card.title.contains(query, ignoreCase = true) ||
                card.subtitle.contains(query, ignoreCase = true) ||
                card.department.contains(query, ignoreCase = true) ||
                card.tags.any { it.contains(query, ignoreCase = true) } ||
                card.details.any { it.title.contains(query, ignoreCase = true) }

            matchCategory && matchQuery
        }
    }.stateIn(externalScope, SharingStarted.Eagerly, emptyList())

    init {
        // Load initial data
        refreshData()
        if (autoStartTicker) {
            startTicker()
        }
    }

    override fun startTicker() {
        tickerJob?.cancel()
        tickerJob = externalScope.launch {
            while (isActive) {
                delay(6000) // Update every 6 seconds for dynamic live feeling
                if (_isAutoRefreshEnabled.value) {
                    refreshData()
                }
            }
        }
    }

    override fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    private fun refreshData() {
        cycleCount++
        val cards = service.getDynamicOpenDataCards(cycleCount)
        _allCards.value = cards
        _lastRefreshedTime.value = timeFormat.format(Date())
    }

    override fun setCategory(category: A2UICategory) {
        _selectedCategory.value = category
    }

    override fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    override fun toggleAutoRefresh() {
        _isAutoRefreshEnabled.value = !_isAutoRefreshEnabled.value
    }

    override fun refresh() {
        externalScope.launch {
            _isRefreshing.value = true
            refreshData()
            delay(400) // Visual confirmation for user
            _isRefreshing.value = false
        }
    }

    override fun getCardById(id: String): A2UICardData? {
        return _allCards.value.find { it.id == id }
    }
}
