package com.example.simplenoteapp.modules.notes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResponsiveLazyGrid(modifier: Modifier = Modifier, notes: List<HashMap<String, Any?>>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(250.dp),
        verticalItemSpacing = 16.dp,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = notes,
            // set the id as key so that the grid can track the items
            key = { note -> note["id"].toString() }
        ) { note ->
            NoteCard(note)
        }
    }
}

@Composable
fun NoteCard(note: HashMap<String, Any?>) {
    var isToggled by remember { mutableStateOf(false) }

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isToggled = !isToggled
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Header(title = note["title"].toString())
            Spacer(modifier = Modifier.height(4.dp))

            val content = note["content"].toString()
            if (content.length > 300 && !isToggled) {
                Text(note["content"].toString().take(300) + " ...")
            } else {
                Text(note["content"].toString())
            }
        }
    }
}

@Composable
fun Header(modifier: Modifier = Modifier, title: String) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        // add title text
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        )

        IconButton(onClick = {

        }) { Icon(Icons.Filled.Edit, contentDescription = "Edit") }
        IconButton(onClick = {

        }) { Icon(Icons.Filled.Clear, contentDescription = "Delete") }
    }
}
