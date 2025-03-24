package xyz.icode.lib.todo

import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.icode.mvs.RxRepo

private const val TAG = "TodoRepo"

class TodoRepo : RxRepo<TodoState> by RxRepo(initState = TodoState()) {
    private val scope = MainScope()

    init {
        scope.launch {
            (0..4).forEach { i ->
                delay(100L * i)
                addTodo(title = "Todo $i")
            }

            onEach {
                Log.d(TAG, "onUpdate state: $it")
            }
        }
    }

    fun addTodo(title: String) {
        setState {
            copy(todos = todos + TodoRow(title = title))
        }
    }
}