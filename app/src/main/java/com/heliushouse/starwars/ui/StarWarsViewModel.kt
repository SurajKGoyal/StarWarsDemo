package com.heliushouse.starwars.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heliushouse.starwars.repository.StarWarsRepository
import com.heliushouse.starwars.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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

}