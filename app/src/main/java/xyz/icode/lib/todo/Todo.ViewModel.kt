package xyz.icode.lib.todo

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.icode.mvs.StateProducer
import xyz.icode.mvs.stateProducer

class TodoViewModel : StateProducer<TodoState> by stateProducer(initState = TodoState()) {
    private val scope = MainScope()

    init {
        scope.launch {
            (0..10).forEach { i ->
                delay(100L * i)
                addTodo(title = "Todo $i")
            }
        }
    }

    fun addTodo(title: String) {
        update {
            copy(todos = todos + TodoRow(title = title))
        }
    }
}