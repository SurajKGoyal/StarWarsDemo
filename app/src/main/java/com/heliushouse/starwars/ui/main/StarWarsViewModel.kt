package com.heliushouse.starwars.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heliushouse.starwars.model.People
import com.heliushouse.starwars.repository.StarWarsRepository
import com.heliushouse.starwars.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarWarsViewModel @Inject constructor(private val repository: StarWarsRepository) :
    ViewModel() {

    private val _response =
        MutableStateFlow<ResponseState>(ResponseState.Loading("Fetching data..."))

    val response: StateFlow<ResponseState> = _response

    fun getPeople() {
        viewModelScope.launch {
            repository.getData()
                .catch { e ->
                    _response.value = ResponseState.Error("Something went wrong!")
                }
                .collect { peoples ->
                    _response.value = ResponseState.Success(peoples)
                }


        }
    }

    fun search(query: String): Flow<List<People>> {
        if (query.isEmpty()) return emptyFlow()
        return flow {
            repository.search(query)
                .catch { e ->
                    emit(emptyList<People>())
                }
                .collect {
                    emit(it.peoples)
                }

        }
    }

}