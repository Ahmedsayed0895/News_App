package com.task.newsapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.newsapp.data.model.Article
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
        collectSavedArticles()
    }

    //region saved article logic
    private var savedArticle: List<Article> = emptyList()

    fun onSaveClick(article: Article) {
        viewModelScope.launch {
            if (article.isSaved) {
                val storedArticle = savedArticle.find { it.url == article.url }
                if (storedArticle != null)
                    newsRepository.deleteArticle(storedArticle)
            } else {
                newsRepository.insertArticle(article.copy(isSaved = true))
            }
        }
    }

    private fun collectSavedArticles() {
        viewModelScope.launch {
            newsRepository.getSavedArticles().collect { articles ->
                savedArticle = articles
                updateArticlesStatus()
            }
        }
    }

    private fun updateArticlesStatus() {
        val savedUrls = savedArticle.map { it.url }
        val updatedArticles = _homeState.value.allNews.map { article ->
            article.copy(isSaved = savedUrls.contains(article.url))
        }
        _homeState.value = _homeState.value.copy(allNews = updatedArticles)
    }

    private fun mapToSavedArticles(articles: List<Article>): List<Article> {
        val savedUrls = savedArticle.map { it.url }
        return articles.map { article ->
            article.copy(isSaved = savedUrls.contains(article.url))
        }
    }

    //endregion

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
                val savedArticles = mapToSavedArticles(response.articles)
                _homeState.value = _homeState.value.copy(
                    allNews = savedArticles,
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

    private fun getNewsByCategory(category: String) {
        viewModelScope.launch {
            try {
                _homeState.value = _homeState.value.copy(isNewsLoading = true)
                val allNews = newsRepository.searchNews(searchQuery = category, pageNumber = 1)
                val savedArticles = mapToSavedArticles(allNews.articles)
                _homeState.value = _homeState.value.copy(
                    allNews = savedArticles,
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
}