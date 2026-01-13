package awm.dev.volume8d_vuvqnphuc.ui.main.list_music

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import awm.dev.volume8d_vuvqnphuc.R

data class MusicFile(val id: Int, val name: String, val duration: String, val author: String)

@Composable
fun ListMusicScreen() {
    // Demo data
    val musicFiles = remember {
        mutableStateListOf(
            MusicFile(1, "Bohemian Rhapsody", "05:55", "Queen"),
            MusicFile(2, "Hotel California", "06:30", "Eagles"),
            MusicFile(3, "Imagine", "03:03", "John Lennon"),
            MusicFile(4, "Shape of You", "03:53", "Ed Sheeran"),
            MusicFile(5, "Blinding Lights", "03:20", "The Weeknd"),
            MusicFile(6, "Stay", "02:21", "The Kid LAROI"),
            MusicFile(7, "Levitating", "03:23", "Dua Lipa"),
            MusicFile(8, "Peaches", "03:18", "Justin Bieber"),
            MusicFile(9, "Good 4 U", "02:58", "Olivia Rodrigo"),
            MusicFile(10, "Bad Guy", "03:14", "Billie Eilish"),
        )
    }

    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Upper Title & Search
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "My Music",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp)),
                placeholder = { Text("Search songs...", color = Color.White.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }

        // List of Music Files
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 100.dp, start = 24.dp, end = 24.dp)
        ) {
            items(
                items = musicFiles.filter { it.name.contains(searchQuery, ignoreCase = true) },
                key = { it.id }
            ) { music ->
                MusicListItem(
                    music = music,
                    onDelete = { musicFiles.remove(music) }
                )
            }
        }
    }
}

@Composable
fun MusicListItem(music: MusicFile, onDelete: () -> Unit) {
    var offsetX by remember { mutableStateOf(0f) }
    val revealWidth = -240f // Khoảng diện tích lộ ra để chứa icon xóa (px)
    var isRemoving by remember { mutableStateOf(false) }

    // Animation xử lý việc thu nhỏ mục khi xóa
    AnimatedVisibility(
        visible = !isRemoving,
        exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
        ) {
            // Lớp nền phía sau chứa Icon Xóa
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFF4E50).copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        isRemoving = true
                        onDelete()
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Confirm Delete",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Lớp nội dung phía trên có thể kéo được
            Row(
                modifier = Modifier
                    .offset(x = (offsetX / 3).dp) // Chia tỷ lệ để khớp với cảm giác kéo dp
                    .fillMaxSize()
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            val newOffset = offsetX + delta
                            // Chỉ cho phép vuốt sang trái (nút âm) và giới hạn khoảng cách
                            if (newOffset <= 0 && newOffset >= revealWidth * 1.5f) {
                                offsetX = newOffset
                            }
                        },
                        onDragStopped = {
                            // Tự động hít (snap) về các vị trí 0 hoặc revealWidth
                            offsetX = if (offsetX < revealWidth / 2) revealWidth else 0f
                        }
                    )
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Music Icon
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_music),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = music.name,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = music.author,
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    )
                }

                // Duration
                Text(
                    text = music.duration,
                    color = Color.Black.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEE0979)
@Composable
fun ListMusicPreview() {
    ListMusicScreen()
}