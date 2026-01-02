package com.example.myfirebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirebase.modeldata.Siswa
import com.example.myfirebase.repositori.RepositorySiswa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

// Sealed interface untuk mendefinisikan status UI
sealed interface StatusUiSiswa {
    data class Success(val siswa: List<Siswa> = listOf()) : StatusUiSiswa
    object Error : StatusUiSiswa
    object Loading : StatusUiSiswa
}

class HomeViewModel(private val repositorySiswa: RepositorySiswa) : ViewModel() {

    // Menggunakan StateFlow (rekomendasi terbaru) untuk menangani state di ViewModel
    private val _statusUiSiswa = MutableStateFlow<StatusUiSiswa>(StatusUiSiswa.Loading)
    val statusUiSiswa: StateFlow<StatusUiSiswa> = _statusUiSiswa.asStateFlow()

    init {
        loadSiswa()
    }

    fun loadSiswa() {
        viewModelScope.launch {
            _statusUiSiswa.value = StatusUiSiswa.Loading
            _statusUiSiswa.value = try {
                val data = repositorySiswa.getDataSiswa()
                StatusUiSiswa.Success(data)
            } catch (e: IOException) {
                StatusUiSiswa.Error
            } catch (e: Exception) {
                StatusUiSiswa.Error
            }
        }
    }
}