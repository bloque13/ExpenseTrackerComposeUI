package com.example.jetpackcompose.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DropdownListView(
    map: Map<Int, String>,
    title: String,
    onItemSelected: (String) -> Unit,
) {
    var selectedText = remember { mutableStateOf("Please Select") }
    var expanded = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.h6
            )
            Text(
                text = selectedText.value,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded.value = true })
                    .background(MaterialTheme.colors.contentColorFor(Color.LightGray))
            )
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .fillMaxWidth()
        ) {

            map.entries.forEach {
                DropdownMenuItem(onClick = {
                    onItemSelected(it.value)
                    selectedText.value = it.value
                    expanded.value = false
                }) {
                    Text(text = it.value)
                }
            }
        }
    }
}