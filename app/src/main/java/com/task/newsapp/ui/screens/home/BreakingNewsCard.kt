package com.task.newsapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.task.newsapp.R
import com.task.newsapp.data.model.Article
import com.task.newsapp.data.model.Source
import com.task.newsapp.ui.theme.PrimaryBlue


@Composable
fun BreakingNewsCard(
    article: Article,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(230.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = PrimaryBlue),

        contentAlignment = Alignment.BottomStart


    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.urlToImage)
                .crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy( CachePolicy.ENABLED)
                .error(R.drawable.ic_place_holder)
                .placeholder(R.drawable.ic_breaking_news_white)
                .build(),
            contentDescription = "Breaking News Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        startY = 100f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = article.source?.name ?: "Unknown Source",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .background(
                        color = PrimaryBlue.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = article.title ?: "No Title",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

    }

}

@Preview
@Composable
fun BreakingNewsCardPreview() {
    BreakingNewsCard(
        article = Article(
            author = "Author",
            content = "Content",
            description = "Description",
            publishedAt = "2023-07-22",
            source = Source(
                id = "1",
                name = "Source"
            ),
            title = "Breaking News Title Breaking News Title Breaking News Title Breaking News Title",
            url = "https://example.com",
            urlToImage = "https://example.com/image.jpg",
            id = null
        ),
        {}
    )
}