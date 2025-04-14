package com.productivitybandits.focuspocusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productivitybandits.focuspocusapp.models.Nudge
import com.productivitybandits.focuspocusapp.repository.NudgesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class NudgesViewModel(private val repository: NudgesRepository) : ViewModel() {

    // 🔁 Holds current list of nudges fetched from backend
    private val _nudges = MutableStateFlow<List<Nudge>>(emptyList())
    val nudges: StateFlow<List<Nudge>> get() = _nudges

    // 🚀 Fetch nudges from the backend (GET /nudges)
    fun fetchNudges() {
        viewModelScope.launch {
            try {
                val result = repository.getNudges() // 👉 Backend should return a list of Nudge objects
                _nudges.value = result
            } catch (e: Exception) {
                e.printStackTrace() // Consider sending this to crash logs
            }
        }
    }

    // ➕ Add a new nudge (POST /nudges)
    fun addNudge(nudge: Nudge) {
        viewModelScope.launch {
            try {
                val newNudge = repository.addNudge(nudge)
                _nudges.value = _nudges.value + newNudge
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ❌ Delete a nudge by ID (DELETE /nudges/{id})
    fun deleteNudge(id:String) {
        viewModelScope.launch {
            try {
                repository.deleteNudge(id)
                _nudges.value = _nudges.value.filterNot { it.id == id }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ Mark a nudge as completed (PUT /nudges/{id}/complete)
    fun markCompleted(id: String) {
        viewModelScope.launch {
            try {
                val updated = repository.markNudgeCompleted(id) // 👉 Backend should return the updated Nudge after marking complete
                _nudges.value = _nudges.value.map { if (it.id == id) updated else it }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}