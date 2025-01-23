package xyz.icode.lib.todo

import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.icode.mvs.MvLogic
import xyz.icode.mvs.stateProducer

private const val TAG = "TodoViewModel"

class TodoLogicMv : MvLogic<TodoState> by stateProducer(initState = TodoState()) {
    private val scope = MainScope()

    init {
        scope.launch {
            (0..4).forEach { i ->
                delay(100L * i)
                addTodo(title = "Todo $i")
            }
        }

        scope.launch {
            onAny {
                Log.d(TAG, "onUpdate state: $it")
            }
        }
    }

    suspend fun addTodo(title: String) {
        update {
            copy(todos = todos + TodoRow(title = title))
        }
    }
}