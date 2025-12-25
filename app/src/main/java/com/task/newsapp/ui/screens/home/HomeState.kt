package com.task.newsapp.ui.screens.home

import com.task.newsapp.data.model.Article

data class HomeState(
    val breakingNews: List<Article> = emptyList(),
    val allNews: List<Article> = emptyList(),
    val isNewsLoading: Boolean = false,
    val isBreakingNewsLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String = "General",
    val isRefresh: Boolean = false,
    val categories: List<String> = listOf( "General","Business", "Entertainment", "Health", "Science", "Sports", "Technology")
)
