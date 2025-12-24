package com.task.newsapp.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.task.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(article: Article): Long

    @Query("SELECT * FROM articles WHERE isBreaking = 0")
    suspend fun getCachedArticles(): List<Article>

    @Query("SELECT * FROM articles WHERE isBreaking = 1")
    suspend fun getCachedBreakingNews(): List<Article>

    @Query("SELECT * FROM articles WHERE isSaved = 1 ")
    fun getSavedArticle(): Flow<List<Article>>

    @Query("DELETE FROM articles WHERE isSaved = 0")
    suspend fun clearCache()

    @Delete
    suspend fun deleteArticle(article: Article)

}