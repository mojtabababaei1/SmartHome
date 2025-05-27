package com.maadiran.myvision.presentation.features.devices.tv.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.maadiran.myvision.presentation.features.devices.tv.ui.theme.RemoteColors.DarkText

data class AppShortcut(
    val name: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconTint: Color = Color.White,
    val onClick: () -> Unit = {}
)

val appShortcuts = listOf(
    AppShortcut(
        "Netflix",
        Icons.Rounded.Movie,
        Color(0xFFE50914)
    ),
    AppShortcut(
        "YouTube",
        Icons.Rounded.PlayCircle,
        Color(0xFFFF0000)
    ),
    AppShortcut(
        "Prime",
        Icons.Rounded.LocalMovies,
        Color(0xFF00A8E1)
    ),
    AppShortcut(
        "Disney+",
        Icons.Rounded.Star,
        Color(0xFF113CCF)
    ),
    AppShortcut(
        "Spotify",
        Icons.Rounded.MusicNote,
        Color(0xFF1DB954)
    ),
    AppShortcut(
        "Games",
        Icons.Rounded.SportsEsports,
        Color(0xFF673AB7)
    )
)

@Composable
fun AppShortcutsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Quick Apps",
            style = MaterialTheme.typography.titleMedium,
            color = DarkText,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(appShortcuts) { app ->
                AppShortcutItem(app)
            }
        }
    }
}


@Composable
fun AppShortcutItem(
    shortcut: AppShortcut
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.width(72.dp)
    ) {
        Surface(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = shortcut.onClick),
            color = shortcut.backgroundColor,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = shortcut.icon,
                    contentDescription = shortcut.name,
                    tint = shortcut.iconTint,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(
            text = shortcut.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}