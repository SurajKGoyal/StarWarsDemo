package com.heliushouse.starwars.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.heliushouse.starwars.R
import com.heliushouse.starwars.databinding.ActivityMainBinding
import com.heliushouse.starwars.model.People
import com.heliushouse.starwars.utils.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel: StarWarsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        addObservers()
    }

    private fun addObservers(){
        viewModel.getPeople()
        lifecycleScope.launch {
            viewModel.response.collect { uiState->
                when(uiState){
                    is ResponseState.Loading -> showDialog(uiState.msg)
                    is ResponseState.Success -> showList(uiState.response.peoples)
                    is ResponseState.Error -> showError(uiState.msg)
                }
            }
        }
    }

    private fun showDialog(message: String) {

    }

    private fun hideDialog() {

    }

    private fun showList(peoples: List<People>){
        hideDialog()
        binding.nameList.adapter = PeopleAdapter(peoples)
    }

    private fun showError(message: String){
        hideDialog()

    }

}