package com.task.newsapp.ui.component


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.task.newsapp.ui.theme.Gray
import com.task.newsapp.ui.theme.PrimaryBlue
import com.task.newsapp.ui.theme.White

@Composable
fun NewsItem(
    article: Article,
    onClick: () -> Unit,
    onSaveClick: (Article) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlToImage)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy( CachePolicy.ENABLED)
                    .error(R.drawable.ic_place_holder)
                    .build(),
                contentDescription = "News Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = article.title ?: "No Title",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = article.description ?: "No Description",
                    style = MaterialTheme.typography.bodySmall.copy(color = Gray),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = article.source?.name ?: "Unknown Source",
                        style = MaterialTheme.typography.labelSmall,
                        color = White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryBlue)
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    )

                    Text(
                        text = article.publishedAt?.substring(0, 10) ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(Modifier.weight(1f))

                    IconButton(
                        onClick = { onSaveClick(article) },
                    ) {
                        Icon(
                            imageVector =
                                if (article.isSaved) Icons.Default.Bookmark
                                else Icons.Default.BookmarkBorder,
                            contentDescription = "Save Article",
                            tint = if (article.isSaved) PrimaryBlue else Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

        }
    }
}


@Preview
@Composable
fun NewsItemPreview() {
    NewsItem(
        article = Article(
            author = "Author",
            content = "Content",
            description = "The new features promise to change The new features promise to chang",
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
        onClick = {},
        onSaveClick = {}
    )
}