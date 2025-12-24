package com.task.newsapp.domain.repository

import com.task.newsapp.data.model.Article
import com.task.newsapp.data.model.NewsResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getBrakingNews(countryCode: String, pageNumber: Int, category: String, searchQuery: String): NewsResponse
    suspend fun searchNews(searchQuery: String, pageNumber: Int): NewsResponse
    fun getSavedArticles(): Flow<List<Article>>
    suspend fun insertArticle(article: Article): Long
    suspend fun deleteArticle(article: Article)
}