package com.example.townpass.ui.youbike

import androidx.lifecycle.ViewModel
import com.example.townpass.data.model.YouBikeFilterMode
import com.example.townpass.data.model.YouBikeStation
import com.example.townpass.data.model.YouBikeStats
import com.example.townpass.data.repository.YouBikeRepository
import com.example.townpass.data.repository.YouBikeRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class YouBikeViewModel(
    private val repository: YouBikeRepository = YouBikeRepositoryImpl()
) : ViewModel() {

    val filteredStations: StateFlow<List<YouBikeStation>> = repository.filteredStations
    val stats: StateFlow<YouBikeStats> = repository.stats
    val districts: StateFlow<List<String>> = repository.districts
    val selectedDistrict: StateFlow<String> = repository.selectedDistrict
    val filterMode: StateFlow<YouBikeFilterMode> = repository.filterMode
    val searchQuery: StateFlow<String> = repository.searchQuery
    val selectedStation: StateFlow<YouBikeStation?> = repository.selectedStation
    val isLoading: StateFlow<Boolean> = repository.isLoading
    val lastUpdatedTime: StateFlow<String> = repository.lastUpdatedTime

    private val _isMapView = MutableStateFlow(true)
    val isMapView: StateFlow<Boolean> = _isMapView.asStateFlow()

    fun setDistrict(district: String) {
        repository.setDistrict(district)
    }

    fun setFilterMode(mode: YouBikeFilterMode) {
        repository.setFilterMode(mode)
    }

    fun setSearchQuery(query: String) {
        repository.setSearchQuery(query)
    }

    fun selectStation(station: YouBikeStation?) {
        repository.selectStation(station)
    }

    fun selectStationBySno(sno: String) {
        val found = filteredStations.value.find { it.sno == sno }
        if (found != null) {
            repository.selectStation(found)
        }
    }

    fun toggleFavorite(sno: String) {
        repository.toggleFavorite(sno)
    }

    fun toggleViewMode() {
        _isMapView.value = !_isMapView.value
    }

    fun refresh() {
        repository.refresh()
    }
}
