package com.darkblue.minimalisttodolistv4.presentation

import com.darkblue.minimalisttodolistv4.data.DeletedTask
import com.darkblue.minimalisttodolistv4.data.RecurrenceType
import com.darkblue.minimalisttodolistv4.data.SortType
import com.darkblue.minimalisttodolistv4.data.Task
import java.time.LocalDate
import java.time.LocalTime

sealed interface TaskEvent {
    object SaveTask: TaskEvent
    data class SetTitle(val title: String): TaskEvent
    data class SetPriority(val priority: Int): TaskEvent
    data class SetNote(val note: String): TaskEvent
    data class SortTasks(val sortType: SortType): TaskEvent

    // AddTask Dialog
    object ShowAddTaskDialog: TaskEvent
    object HideAddTaskDialog: TaskEvent

    // Menu Dialog
    object ShowMenuDialog: TaskEvent
    object HideMenuDialog: TaskEvent

    // History Dialog
    object ShowHistoryDialog: TaskEvent
    object HideHistoryDialog: TaskEvent

    // Date Picker
    object ShowDatePicker: TaskEvent
    object HideDatePicker: TaskEvent
    data class SetDueDate(val dueDate: LocalDate): TaskEvent

    // Time Picker
    object ShowTimePicker: TaskEvent
    object HideTimePicker: TaskEvent
    data class SetDueTime(val dueTime: LocalTime): TaskEvent

    // Recurrence
    data class SetRecurrenceType(val recurrenceType: RecurrenceType) : TaskEvent
    data class SetRecurrenceFilter(val recurrenceType: RecurrenceType) : TaskEvent

    data class EditTask(val task: Task) : TaskEvent

    // Deletion + History
    data class DeleteTask(val task: Task): TaskEvent
    data class DeleteForever(val deletedTask: DeletedTask) : TaskEvent
    data class UndoDeleteTask(val deletedTask: DeletedTask) : TaskEvent
}
