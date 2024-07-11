package com.minimalisttodolist.pleasebethelastrecyclerview.viewmodel

import com.minimalisttodolist.pleasebethelastrecyclerview.data.model.DeletedTask
import com.minimalisttodolist.pleasebethelastrecyclerview.data.model.RecurrenceType
import com.minimalisttodolist.pleasebethelastrecyclerview.data.model.SortType
import com.minimalisttodolist.pleasebethelastrecyclerview.data.model.Task
import java.time.LocalDate
import java.time.LocalTime

sealed interface TaskEvent {
    data object SaveTaskAndHideAddTaskDialog : TaskEvent
    data class SetTitle(val title: String) : TaskEvent
    data class SetPriority(val priority: Int) : TaskEvent
    data class SetNote(val note: String) : TaskEvent
    data class SortTasks(val sortType: SortType) : TaskEvent

    // AddTask Dialog
    data object ShowAddTaskDialog : TaskEvent

    // Date Picker
    data object ShowDatePicker : TaskEvent
    data object HideDatePicker : TaskEvent
    data class SetDueDate(val dueDate: LocalDate) : TaskEvent

    // Time Picker
    data object ShowTimePicker : TaskEvent
    data object HideTimePicker : TaskEvent
    data class SetDueTime(val dueTime: LocalTime) : TaskEvent

    // Recurrence
    data class SetRecurrenceType(val recurrenceType: RecurrenceType) : TaskEvent
    data class SetRecurrenceFilter(val recurrenceType: RecurrenceType) : TaskEvent

    data class EditTask(val task: Task) : TaskEvent

    // Deletion + History
    data class DeleteTask(val task: Task) : TaskEvent
    data class DeleteForever(val deletedTask: DeletedTask) : TaskEvent
    data class UndoDeleteTask(val deletedTask: DeletedTask) : TaskEvent
    data object DeleteAllHistoryTasks : TaskEvent

    data object RefreshTasks : TaskEvent
}