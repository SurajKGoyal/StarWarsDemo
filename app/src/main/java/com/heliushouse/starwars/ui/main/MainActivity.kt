package com.heliushouse.starwars.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.heliushouse.starwars.R
import com.heliushouse.starwars.databinding.ActivityMainBinding
import com.heliushouse.starwars.model.People
import com.heliushouse.starwars.utils.ResponseState
import com.heliushouse.starwars.utils.getQueryTextChangeStateFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchViewItem: MenuItem = menu!!.findItem(R.id.app_bar_search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        lifecycleScope.launch {
            searchView.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    return@filter query.isNotEmpty()
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    viewModel.search(query)
                        .catch {
                            emitAll(flowOf(emptyList()))
                        }
                }
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    binding.nameList.adapter = PeopleAdapter(result)
                }
        }
        return super.onCreateOptionsMenu(menu)
    }

   /* private fun initListener() {
        lifecycleScope.launch {
            binding.searchPeople.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    return@filter query.isNotEmpty()
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    viewModel.search(query)
                        .catch {
                            emitAll(flowOf(emptyList()))
                        }
                }
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    binding.nameList.adapter = PeopleAdapter(result)
                }
        }
    }
*/
    private fun addObservers() {
        viewModel.getPeople()
        lifecycleScope.launch {
            viewModel.response.collect { uiState ->
                when (uiState) {
                    is ResponseState.Loading -> showLoading(uiState.msg)
                    is ResponseState.Success -> showList(uiState.response.peoples)
                    is ResponseState.Error -> showError(uiState.msg)
                }
            }
        }
    }

    private fun showLoading(message: String) {
        binding.loading = true
    }

    private fun hideDialog() {
        binding.loading = false
    }

    private fun showList(peoples: List<People>) {
        hideDialog()
        binding.nameList.adapter = PeopleAdapter(peoples)
    }

    private fun showError(message: String) {
        hideDialog()

    }

    companion object{
        fun startActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

}