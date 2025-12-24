package com.task.newsapp.ui.screens.favoriteArticles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.task.newsapp.ui.screens.home.NewsItem
import com.task.newsapp.ui.screens.home.navigateToDetails
import com.task.newsapp.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteArticleScreen(
    navController: NavController,
    viewModel: FavoriteArticlesViewModel = hiltViewModel()
) {
    val articles by viewModel.favoritesState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Saved Articles",
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF3F4F6),
                    titleContentColor = PrimaryBlue
                )
            )

        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->

        if (articles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No favorites yet ️", color = PrimaryBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(articles) { article ->
                    NewsItem(
                        article = article,
                        onClick = { navigateToDetails(navController, article.url) },
                        onSaveClick = { viewModel.deleteArticle(article) }
                    )
                }
            }
        }
    }
}