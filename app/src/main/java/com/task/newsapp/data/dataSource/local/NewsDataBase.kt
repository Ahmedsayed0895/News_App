package com.task.newsapp.data.dataSource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.task.newsapp.data.Converters
import com.task.newsapp.data.dataSource.local.dao.ArticleDao
import com.task.newsapp.data.dataSource.local.dao.UserDao
import com.task.newsapp.data.model.Article
import com.task.newsapp.data.model.User

@Database(entities = [User::class, Article::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun articleDao(): ArticleDao
}