package com.task.newsapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()
    var searchJob: Job? = null

    init {
        getBreakingNews()
        getNewsByCategory("General")
    }


    private fun getBreakingNews(
        category: String = "general",
        searchQuery: String = ""
    ) {
        viewModelScope.launch {
            try {
                _homeState.value = _homeState.value.copy(isBreakingNewsLoading = true)
                val breakingNews = newsRepository.getBrakingNews(
                    pageNumber = 1,
                    countryCode = "us",
                    category = category,
                    searchQuery = searchQuery
                )
                _homeState.value = _homeState.value.copy(
                    breakingNews = breakingNews.articles,
                    isBreakingNewsLoading = false
                )
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    error = e.message ?: "An Error Occurred",
                    isBreakingNewsLoading = false
                )
            }
        }
    }

    private fun getNewsByCategory(category: String) {
        viewModelScope.launch {
            try {
                _homeState.value = _homeState.value.copy(isNewsLoading = true)
                val allNews = newsRepository.searchNews(searchQuery = category, pageNumber = 1)
                _homeState.value = _homeState.value.copy(
                    allNews = allNews.articles,
                    isNewsLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    error = e.message ?: "An Error Occurred",
                    isBreakingNewsLoading = false,
                    isNewsLoading = false
                )
            }
        }
    }

    fun onCategorySelected(category: String) {
        _homeState.value = _homeState.value.copy(selectedCategory = category)
        getNewsByCategory(category)
        getBreakingNews(category)
    }

    fun onSearchQueryChanged(query: String) {
        _homeState.value = _homeState.value.copy(searchQuery = query)
        if (query.isBlank()) {
            getNewsByCategory(_homeState.value.selectedCategory)
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                delay(500)
                _homeState.value = _homeState.value.copy(isNewsLoading = true)
                val response = newsRepository.searchNews(query, 1)
                _homeState.value = _homeState.value.copy(
                    allNews = response.articles,
                    isNewsLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    error = e.message ?: "An Error Occurred",
                )

            }
        }
    }
}