package com.minimalisttodolist.pleasebethelastrecyclerview.ui.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minimalisttodolist.pleasebethelastrecyclerview.data.model.PriorityColor
import com.minimalisttodolist.pleasebethelastrecyclerview.data.model.RecurrenceType
import com.minimalisttodolist.pleasebethelastrecyclerview.data.model.Task
import com.minimalisttodolist.pleasebethelastrecyclerview.ui.components.CustomBox
import com.minimalisttodolist.pleasebethelastrecyclerview.ui.screens.CompleteIcon
import com.minimalisttodolist.pleasebethelastrecyclerview.ui.screens.DueDate_Recurrence_Note
import com.minimalisttodolist.pleasebethelastrecyclerview.ui.theme.LocalDarkTheme
import com.minimalisttodolist.pleasebethelastrecyclerview.util.vibrate
import com.minimalisttodolist.pleasebethelastrecyclerview.viewmodel.TaskViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Tutorial(
    onDismiss: () -> Unit,
    onDisable: () -> Unit,
    onShowAddTaskDialog: () -> Unit,
    onShowMenuDialog: () -> Unit,
    onEdit: (Task) -> Unit,
    viewModel: TaskViewModel
) {
    val steps = listOf("Tap to add a task", "Press and hold for menu", "Tap a task to edit")
    val pagerState = rememberPagerState(pageCount = { steps.size })
    val context = LocalContext.current

    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        CustomBox {
            Column(
                modifier = Modifier
                    .padding(16.dp)
//                    .fillMaxWidth()
                    .width(350.dp)
//                    .width(IntrinsicSize.Max)
                ,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/${steps.size}",
                    modifier = Modifier.align(Alignment.End),
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodySmall,
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                when (page) {
                                    0 -> {
                                        Modifier.clickable {
                                            onShowAddTaskDialog()
                                        }
                                    }
                                    1 -> {
                                        Modifier.combinedClickable(
                                                onLongClick = {
                                                    vibrate(context = context, strength = 1)
                                                    onShowMenuDialog()
                                                },
                                                onClick = { }
                                            )
                                    }
                                    2 -> {
                                        Modifier.clickable {
                                            onShowAddTaskDialog()
                                        }
                                    }
                                    else -> { Modifier }
                                }
                            )
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        when (page) {
                            0 -> {
                                AnimatedClickIcon()
                            }
                            1 -> {
                                AnimatedLongPressIcon()
                            }
                            2 -> {
                                val task = Task(
                                    title = "Task",
                                    priority = 3,
                                    note = "Note",
                                    recurrenceType = RecurrenceType.WEEKLY,
                                    dueDate = null
                                )
                                TutorialTaskItem(
                                    onEdit = {
                                        onEdit(task)
                                        onDismiss()
                                    },
                                    onDelete = {},
                                    task = task,
                                    viewModel = viewModel
                                )
                            }
                        }
                        Text(
                            text = steps[page],
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        steps.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = if (index == pagerState.currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiaryContainer,
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    Text(
                        "Disable Tutorial",
                        color = if(pagerState.currentPage == steps.lastIndex) MaterialTheme.colorScheme.tertiary else Color.Transparent,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .clickable {
                                if (pagerState.currentPage == steps.lastIndex) {
                                    onDisable()
                                }
                            }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TutorialTaskItem(task: Task, onEdit: (Task) -> Unit, onDelete: (Task) -> Unit, viewModel: TaskViewModel) {
    val darkTheme = LocalDarkTheme.current

    val priorityColor = when (task.priority) {
        0 -> PriorityColor.PRIORITY0.getColor(darkTheme)
        1 -> PriorityColor.PRIORITY1.getColor(darkTheme)
        2 -> PriorityColor.PRIORITY2.getColor(darkTheme)
        3 -> PriorityColor.PRIORITY3.getColor(darkTheme)
        else -> Color.Gray
    }

    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = "Priority",
            tint = priorityColor,
            modifier = Modifier.size(24.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .clickable {
                    onEdit(task)
                }
        ) {
            Text(
                task.title,
                style = MaterialTheme.typography.titleLarge,
            )
            DueDate_Recurrence_Note(task = task, viewModel = viewModel)
        }
        CompleteIcon(
            onDelete = { onDelete(task) }
        )
    }
}

@Composable
fun AnimatedClickIcon(modifier: Modifier = Modifier) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.9f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "Animated Click Icon"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.7f else 1f,
        animationSpec = tween(durationMillis = 100), label = "Animate State for alpha"
    )

    LaunchedEffect(Unit) {
        while (true) {
            // Simulate click
            isPressed = true
            delay(250)
            isPressed = false
            delay(1000)
        }
    }

    Icon(
        imageVector = Icons.Outlined.Add,
        contentDescription = "Add task",
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                alpha = alpha
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(percent = 25)
            )
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(percent = 7)),
        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = alpha)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedLongPressIcon(modifier: Modifier = Modifier) {
    var isPressed by remember { mutableStateOf(false) }
    var isLongPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = when {
            isLongPressed -> 1.2f
            isPressed -> 0.9f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "Animate Long Press Icon"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isPressed || isLongPressed) 0.7f else 1f,
        animationSpec = tween(durationMillis = 100), label = "Animate Float State for alpha"
    )

    LaunchedEffect(Unit) {
        while (true) {
            // Simulate long press
            isPressed = true
            delay(500)
            isLongPressed = true
            delay(1000)
            isLongPressed = false
            isPressed = false
            delay(1000)
        }
    }

    Icon(
        imageVector = Icons.Outlined.Add,
        contentDescription = "Add task",
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                alpha = alpha
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(percent = 25)
            )
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(percent = 7)),
        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = alpha)
    )
}
