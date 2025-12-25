package com.task.newsapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.newsapp.data.model.Article
import com.task.newsapp.data.util.NetworkExceptions
import com.task.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()
    var newsPage = 1
    private var isPaginating = false
    private var searchJob: Job? = null


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
        newsPage = 1
        _homeState.value = _homeState.value.copy(selectedCategory = category, allNews = emptyList())
        getNewsByCategory(category)
        getBreakingNews(category)
    }


    fun onSearchQueryChanged(query: String) {

        newsPage = 1
        _homeState.value = _homeState.value.copy(searchQuery = query)
        if (query.isBlank()) {
            getNewsByCategory(_homeState.value.selectedCategory)
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            try {
                if (newsPage == 1) {
                    _homeState.value = _homeState.value.copy(isNewsLoading = true)
                }
                if (isPaginating && newsPage > 1) return@launch
                isPaginating = true
                val response = newsRepository.searchNews(query, newsPage)
                val savedArticles = mapToSavedArticles(response.articles)
                val paginatedNews = if (newsPage == 1) {
                    savedArticles
                } else {
                    _homeState.value.allNews + savedArticles
                }
                _homeState.value = _homeState.value.copy(
                    allNews = paginatedNews,
                    isNewsLoading = false,
                    error = null
                )
                newsPage++
                isPaginating = false
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                val errorMessages = NetworkExceptions.getErrorMessage(e)

                _homeState.value = _homeState.value.copy(
                    error = errorMessages,
                    isNewsLoading = false
                )
                isPaginating = false

            }
        }
    }

    fun getNewsByCategory(category: String) {
        viewModelScope.launch {
            try {
                if (newsPage == 1) {
                    _homeState.value = _homeState.value.copy(isNewsLoading = true)
                }
                if (isPaginating && newsPage > 1) return@launch
                isPaginating = true
                val allNews =
                    newsRepository.searchNews(searchQuery = category, pageNumber = newsPage)
                val savedArticles = mapToSavedArticles(allNews.articles)
                val paginatedNews = if (newsPage == 1) {
                    savedArticles
                } else {
                    _homeState.value.allNews + savedArticles
                }
                _homeState.value = _homeState.value.copy(
                    allNews = paginatedNews,
                    isNewsLoading = false,
                    error = null
                )
                newsPage++
                isPaginating = false
            } catch (e: Exception) {
                val errorMessages = NetworkExceptions.getErrorMessage(e)
                val cachedNews = newsRepository.getCachedNews()
                if (cachedNews.isNotEmpty()) {
                    _homeState.value = _homeState.value.copy(
                        allNews = cachedNews,
                        isNewsLoading = false
                    )
                } else {

                    _homeState.value = _homeState.value.copy(
                        error = errorMessages,
                        isBreakingNewsLoading = false,
                        isNewsLoading = false
                    )
                }
                isPaginating = false

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
                val errorMessages = NetworkExceptions.getErrorMessage(e)
                val cachedBreakingNews = newsRepository.getCachedBreakingNews()
                if (cachedBreakingNews.isNotEmpty()) {
                    _homeState.value = _homeState.value.copy(
                        breakingNews = cachedBreakingNews,
                        isBreakingNewsLoading = false
                    )
                } else {
                    _homeState.value = _homeState.value.copy(
                        error = errorMessages,
                        isBreakingNewsLoading = false
                    )
                }

            }
        }
    }
}