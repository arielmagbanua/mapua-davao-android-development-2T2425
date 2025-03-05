package com.example.jackenpoy.modules.game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun HandButtons(
    modifier: Modifier = Modifier,
    onChoiceSelected: (Hand) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(onClick = { onChoiceSelected(Hand.ROCK) }) {
            Text("Rock")
        }
        Button(onClick = { onChoiceSelected(Hand.PAPER) }) {
            Text("Paper")
        }
        Button(onClick = { onChoiceSelected(Hand.SCISSORS) }) {
            Text("Scissors")
        }
    }
}