package com.task.newsapp.data.repository

import com.task.newsapp.data.dataSource.local.dao.ArticleDao
import com.task.newsapp.data.dataSource.remote.NewsApi
import com.task.newsapp.data.model.Article
import com.task.newsapp.data.model.NewsResponse
import com.task.newsapp.domain.repository.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val articleDao: ArticleDao
) : NewsRepository {

    override suspend fun getBrakingNews(
        countryCode: String,
        pageNumber: Int,
        category: String,
        searchQuery: String,
    ): NewsResponse = newsApi.getBrakingNews(
        countryCode = countryCode,
        pageNumber = pageNumber,
        category = category,
        searchQuery = searchQuery
    )


    override suspend fun searchNews(
        searchQuery: String,
        pageNumber: Int
    ): NewsResponse = newsApi.searchForNews(searchQuery, pageNumber)

    override fun getSavedArticles(): Flow<List<Article>> = articleDao.getAllArticles()

    override suspend fun insertArticle(article: Article): Long = articleDao.insertArticles(article)

    override suspend fun deleteArticle(article: Article) = articleDao.deleteArticle(article)
}