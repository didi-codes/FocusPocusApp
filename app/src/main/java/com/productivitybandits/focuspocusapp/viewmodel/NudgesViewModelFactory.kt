package com.productivitybandits.focuspocusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.productivitybandits.focuspocusapp.repository.NudgesRepository


class NudgesViewModelFactory (
    private val repository: NudgesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NudgesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NudgesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}