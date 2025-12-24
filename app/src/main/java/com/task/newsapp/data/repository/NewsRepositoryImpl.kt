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
    ): NewsResponse {
        val response =
            newsApi.getBrakingNews(
                countryCode = countryCode,
                pageNumber = pageNumber,
                category = category,
                searchQuery = searchQuery
            )
        if (pageNumber == 1) {
            articleDao.clearCache()
            response.articles.forEach { article ->
                article.isBreaking = true
                articleDao.insertArticles(article)
            }
        }
        return response
    }

    override suspend fun getCachedBreakingNews(): List<Article> = articleDao.getCachedBreakingNews()

    override suspend fun getCachedNews(): List<Article> = articleDao.getCachedArticles()


    override suspend fun searchNews(
        searchQuery: String,
        pageNumber: Int
    ): NewsResponse {
        val response = newsApi.searchForNews(searchQuery, pageNumber)
        if (response.articles.isNotEmpty()) {
            response.articles.forEach { article ->
                article.isBreaking = false
                articleDao.insertArticles(article)
            }
        }
        return response
    }


    override fun getSavedArticles(): Flow<List<Article>> = articleDao.getSavedArticle()

    override suspend fun insertArticle(article: Article): Long = articleDao.insertArticles(article)

    override suspend fun deleteArticle(article: Article) = articleDao.deleteArticle(article)
}