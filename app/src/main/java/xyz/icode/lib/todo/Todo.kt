package xyz.icode.lib.todo

import xyz.icode.mvs.MvState

data class TodoRow(val title: String)

data class TodoState(val todos: List<TodoRow> = emptyList()): MvState