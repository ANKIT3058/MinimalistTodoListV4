package com.darkblue.minimalisttodolistv4.viewmodel

import com.darkblue.minimalisttodolistv4.data.model.DeletedTask
import com.darkblue.minimalisttodolistv4.data.model.RecurrenceType
import com.darkblue.minimalisttodolistv4.data.model.SortType
import com.darkblue.minimalisttodolistv4.data.model.Task
import java.time.LocalDate
import java.time.LocalTime

sealed interface TaskEvent {
    data object SaveTask: TaskEvent
    data class SetTitle(val title: String): TaskEvent
    data class SetPriority(val priority: Int): TaskEvent
    data class SetNote(val note: String): TaskEvent
    data class SortTasks(val sortType: SortType): TaskEvent

    // AddTask Dialog
    data object ShowAddTaskDialog: TaskEvent
    data object HideAddTaskDialog: TaskEvent

    // Date Picker
    data object ShowDatePicker: TaskEvent
    data object HideDatePicker: TaskEvent
    data class SetDueDate(val dueDate: LocalDate): TaskEvent

    // Time Picker
    data object ShowTimePicker: TaskEvent
    data object HideTimePicker: TaskEvent
    data class SetDueTime(val dueTime: LocalTime): TaskEvent

    // Recurrence
    data class SetRecurrenceType(val recurrenceType: RecurrenceType) : TaskEvent
    data class SetRecurrenceFilter(val recurrenceType: RecurrenceType) : TaskEvent

    data class EditTask(val task: Task) : TaskEvent

    // Deletion + History
    data class DeleteTask(val task: Task): TaskEvent
    data class DeleteForever(val deletedTask: DeletedTask) : TaskEvent
    data class UndoDeleteTask(val deletedTask: DeletedTask) : TaskEvent
    data object DeleteAllHistoryTasks : TaskEvent

    data object RefreshTasks : TaskEvent
}
