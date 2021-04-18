package com.example.jetpackcompose.presentation.components.account

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.DismissDirection.EndToStart
import androidx.compose.material.DismissDirection.StartToEnd
import androidx.compose.material.DismissValue.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.domain.model.Transaction
import com.example.jetpackcompose.toDateLabel
import com.example.jetpackcompose.transactionColorise
import com.example.jetpackcompose.transactionFormat
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun TransactionView(
    transaction: Transaction,
    onDelete: (Int) -> Unit,
    onCancelDelete:() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 2.dp, start = 8.dp, end = 8.dp)
    ) {
        var unread by remember { mutableStateOf(false) }
        val dismissState = rememberDismissState(
            confirmStateChange = {
                if (it == DismissedToEnd) unread = !unread
                it != DismissedToEnd
            }
        )
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier.padding(vertical = 4.dp),
            directions = setOf(EndToStart),
            dismissThresholds = { direction ->
                FractionalThreshold(if (direction == StartToEnd) 0.25f else 0.5f)
            },
            background = {
                val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                val color by animateColorAsState(
                    when (dismissState.targetValue) {
                        DismissValue.Default -> Color.LightGray
                        DismissedToEnd -> Color.Green
                        DismissedToStart -> Color.Red
                    }
                )
                val alignment = when (direction) {
                    StartToEnd -> Alignment.CenterStart
                    EndToStart -> Alignment.CenterEnd
                }
                val icon = when (direction) {
                    StartToEnd -> Icons.Default.Done
                    EndToStart -> Icons.Default.Delete
                }
                val scale by animateFloatAsState(
                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(horizontal = 20.dp),
                    contentAlignment = alignment,
                ) {

                    Row {
                        IconButton(
                            onClick = {
                                onCancelDelete()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Localized description",
                                modifier = Modifier.scale(scale),
                            )
                        }
                        IconButton(
                            onClick = {
                                onDelete(transaction.id)
                            },
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Localized description",
                                modifier = Modifier.scale(scale),
                            )
                        }
                    }

                }
            },
            dismissContent = {
                Card(
                    elevation = animateDpAsState(
                        if (dismissState.dismissDirection != null) 4.dp else 0.dp
                    ).value
                ) {
                    ListItem(
                        text = {
                            Text(
                                text = transaction.category.name,
                                fontWeight = if (unread) FontWeight.Bold else null
                            )
                        },
                        secondaryText = {
                            Text(
                                text = transaction.dateCreated.toDateLabel(),
                                style = MaterialTheme.typography.caption
                            )
                        },
                        trailing = {
                            Text(
                                text = transaction.transactionFormat(),
                                color = transaction.transactionColorise(),
                                style = MaterialTheme.typography.caption
                            )
                        }
                    )
                }
            }
        )

    }
}