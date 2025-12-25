package com.task.newsapp.ui.screens.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.task.newsapp.data.model.Article
import com.task.newsapp.ui.component.AppTextField
import com.task.newsapp.ui.component.BreakingNewsCard
import com.task.newsapp.ui.component.LottieAnimated
import com.task.newsapp.ui.component.NewsItem
import com.task.newsapp.ui.Routes
import com.task.newsapp.ui.component.LottieType
import com.task.newsapp.ui.screens.ErrorScreen
import com.task.newsapp.ui.theme.Gray
import com.task.newsapp.ui.theme.PrimaryBlue
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.homeState.collectAsState()
    HomeScreenContent(
        navController = navController,
        state = state,
        onCategorySelected = viewModel::onCategorySelected,
        onQueryChange = viewModel::onSearchQueryChanged,
        onRetry = viewModel::getNewsByCategory,
        onSaveClick = viewModel::onSaveClick,
        onRefresh = viewModel::onRefresh
    )

}

@Composable
fun HomeScreenContent(
    navController: NavController,
    state: HomeState,
    onCategorySelected: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onRetry: (String) -> Unit,
    onSaveClick: (Article) -> Unit,
    onRefresh: () -> Unit

    ) {

    PullToRefreshBox(
        isRefreshing = state.isRefresh,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .background(Color(0xFFF3F4F6))
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                SearchBarSection(
                    query = state.searchQuery,
                    onQueryChange = onQueryChange
                )
            }
            stickyHeader {
                CategoryChips(
                    categories = state.categories,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }
            if (state.isNewsLoading && state.isBreakingNewsLoading) {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LottieAnimated(LottieType.LOADING)
                    }
                }
            } else if (state.error != null) {
                item {
                    ErrorScreen(
                        message = state.error, onRetry = { onRetry(state.selectedCategory) })
                }

            } else {

                item {
                    Text(
                        text = "Breaking News",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Gray
                        ),
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.breakingNews) { article ->
                            BreakingNewsCard(article = article) {
                                navigateToDetails(navController, article.url)
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Latest News",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Gray
                        ),
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 24.dp,
                        )
                    )
                }
                items(state.allNews.size) { index ->
                    val article = state.allNews[index]
                    if (index >= state.allNews.size - 1 && !state.isNewsLoading) {
                        onRetry
                    }
                    NewsItem(
                        article = article,
                        onClick = { navigateToDetails(navController, article.url) },
                        onSaveClick = { onSaveClick(it) })


                }
            }


        }
    }
}


@Composable
private fun SearchBarSection(query: String, onQueryChange: (String) -> Unit) {
    AppTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholderText = "Search news...",
        leadingIcon = Icons.Default.Search,
    )
}

@Composable
private fun CategoryChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3F4F6))
            .padding(vertical = 8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryBlue,
                    selectedLabelColor = Color.White,
                    labelColor = PrimaryBlue,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = PrimaryBlue,
                ),
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}

fun navigateToDetails(navController: NavController, url: String?) {
    if (url != null) {
        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
        navController.navigate("${Routes.DETAILS}/$encodedUrl")
        println("Opening URL: $url")
    }
}
