package com.example.townpass.data.repository

import com.example.townpass.data.model.YouBikeFilterMode
import com.example.townpass.data.model.YouBikeStation
import com.example.townpass.data.model.YouBikeStats
import com.example.townpass.data.service.YouBikeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface YouBikeRepository {
    val allStations: StateFlow<List<YouBikeStation>>
    val filteredStations: StateFlow<List<YouBikeStation>>
    val stats: StateFlow<YouBikeStats>
    val districts: StateFlow<List<String>>
    val selectedDistrict: StateFlow<String>
    val filterMode: StateFlow<YouBikeFilterMode>
    val searchQuery: StateFlow<String>
    val selectedStation: StateFlow<YouBikeStation?>
    val isLoading: StateFlow<Boolean>
    val lastUpdatedTime: StateFlow<String>

    fun setDistrict(district: String)
    fun setFilterMode(mode: YouBikeFilterMode)
    fun setSearchQuery(query: String)
    fun selectStation(station: YouBikeStation?)
    fun toggleFavorite(sno: String)
    fun refresh()
    fun startTicker()
    fun stopTicker()
}

class YouBikeRepositoryImpl(
    private val service: YouBikeService = YouBikeService(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
    private val autoStartTicker: Boolean = true
) : YouBikeRepository {

    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.TAIWAN)
    private var tickerJob: Job? = null
    private val favoriteIds = mutableSetOf<String>()

    private val _allStations = MutableStateFlow<List<YouBikeStation>>(emptyList())
    override val allStations: StateFlow<List<YouBikeStation>> = _allStations.asStateFlow()

    private val _selectedDistrict = MutableStateFlow("全部")
    override val selectedDistrict: StateFlow<String> = _selectedDistrict.asStateFlow()

    private val _filterMode = MutableStateFlow(YouBikeFilterMode.ALL)
    override val filterMode: StateFlow<YouBikeFilterMode> = _filterMode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    override val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedStation = MutableStateFlow<YouBikeStation?>(null)
    override val selectedStation: StateFlow<YouBikeStation?> = _selectedStation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _lastUpdatedTime = MutableStateFlow(timeFormat.format(Date()))
    override val lastUpdatedTime: StateFlow<String> = _lastUpdatedTime.asStateFlow()

    override val districts: StateFlow<List<String>> = _allStations.map { stations ->
        val list = mutableListOf("全部")
        val uniqueDistricts = stations.map { it.district }.filter { it.isNotBlank() }.distinct().sorted()
        list.addAll(uniqueDistricts)
        list
    }.stateIn(scope, SharingStarted.Eagerly, listOf("全部"))

    override val stats: StateFlow<YouBikeStats> = _allStations.map { stations ->
        var totalBikes = 0
        var totalEmpty = 0
        var totalSpaces = 0
        var activeCount = 0

        for (st in stations) {
            totalBikes += st.availableBikes
            totalEmpty += st.emptySpaces
            totalSpaces += st.totalSpaces
            if (st.isActive) activeCount++
        }

        YouBikeStats(
            totalStations = stations.size,
            totalAvailableBikes = totalBikes,
            totalEmptySpaces = totalEmpty,
            totalSpaces = totalSpaces,
            activeStations = activeCount
        )
    }.stateIn(scope, SharingStarted.Eagerly, YouBikeStats())

    override val filteredStations: StateFlow<List<YouBikeStation>> = combine(
        _allStations,
        _selectedDistrict,
        _filterMode,
        _searchQuery
    ) { stations, district, mode, query ->
        stations.filter { st ->
            val matchDistrict = (district == "全部") || (st.district == district)
            val matchMode = when (mode) {
                YouBikeFilterMode.ALL -> true
                YouBikeFilterMode.RENT -> st.availableBikes > 0
                YouBikeFilterMode.RETURN -> st.emptySpaces > 0
            }
            val matchQuery = query.isBlank() ||
                st.name.contains(query, ignoreCase = true) ||
                st.address.contains(query, ignoreCase = true) ||
                st.district.contains(query, ignoreCase = true)

            matchDistrict && matchMode && matchQuery
        }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

    init {
        // Load stations
        loadStations()
        if (autoStartTicker) {
            startTicker()
        }
    }

    private fun loadStations() {
        scope.launch {
            _isLoading.value = true
            val fetched = service.fetchLiveStations()
            val mapped = fetched.map { st ->
                st.copy(isFavorite = favoriteIds.contains(st.sno))
            }
            _allStations.value = mapped
            _lastUpdatedTime.value = timeFormat.format(Date())

            // Auto-select first station if none selected
            if (_selectedStation.value == null && mapped.isNotEmpty()) {
                _selectedStation.value = mapped.first()
            }
            _isLoading.value = false
        }
    }

    override fun setDistrict(district: String) {
        _selectedDistrict.value = district
    }

    override fun setFilterMode(mode: YouBikeFilterMode) {
        _filterMode.value = mode
    }

    override fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    override fun selectStation(station: YouBikeStation?) {
        _selectedStation.value = station
    }

    override fun toggleFavorite(sno: String) {
        if (favoriteIds.contains(sno)) {
            favoriteIds.remove(sno)
        } else {
            favoriteIds.add(sno)
        }
        _allStations.value = _allStations.value.map { st ->
            if (st.sno == sno) st.copy(isFavorite = favoriteIds.contains(sno)) else st
        }
        if (_selectedStation.value?.sno == sno) {
            _selectedStation.value = _selectedStation.value?.copy(isFavorite = favoriteIds.contains(sno))
        }
    }

    override fun refresh() {
        loadStations()
    }

    override fun startTicker() {
        tickerJob?.cancel()
        tickerJob = scope.launch {
            while (isActive) {
                delay(30000) // Polling interval: 30 seconds
                loadStations()
            }
        }
    }

    override fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }
}
