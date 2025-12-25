package com.task.newsapp.di

import android.app.Application
import androidx.room.Room
import com.task.newsapp.data.dataSource.remote.Constants.BASE_URL
import com.task.newsapp.data.dataSource.local.NewsDataBase
import com.task.newsapp.data.dataSource.remote.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideNewsApi(): NewsApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApi::class.java)

    @Provides
    @Singleton
    fun providesDAtaBase(app: Application): NewsDataBase =
        Room.databaseBuilder(
            app,
            NewsDataBase::class.java,
            "new_db.db",
        ).fallbackToDestructiveMigration(false).build()

    @Singleton
    @Provides
    fun providesUserDao(db: NewsDataBase) = db.userDao()

    @Singleton
    @Provides
    fun providesArticleDao(db: NewsDataBase) = db.articleDao()
}