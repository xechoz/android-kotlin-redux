package xyz.icode.lib.todo

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import xyz.icode.lib.ui.theme.MvStateTheme

private const val TAG = "TodoMain"

@Composable
fun TodoMain(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val model = remember { TodoLogicMv() }
    val todoRows = remember { mutableStateOf(emptyList<TodoRow>()) }
    val context = LocalContext.current

    LaunchedEffect(key1 = todoRows) {
        scope.launch {
            model.onAny(TodoState::todos) {
                Log.d(TAG, "onUpdate todos: $it")
                todoRows.value = it
            }

            model.onAny {
                Log.d(TAG, "onUpdate state: $it")
            }
        }

        scope.launch {
            model.onAny {
                Log.d(TAG, "onUpdate state size: ${it.todos.size}")

                if (it.todos.size >= 10) {
                    Log.d(TAG, "cancel")
                    scope.cancel("cancel")

                    Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LazyColumn(modifier = modifier) {
        item(key = "todo_adder") {
            TodoAdder(scope, model)
        }

        item(key = "todo_rows") {
            todoRows.value.forEach { TodoRow(it) }
        }
    }
}

@Composable
private fun TodoAdder(scope: CoroutineScope, model: TodoLogicMv) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = {
            scope.launch {
                model.addTodo("New Todo ${model.state.todos.size}")
            }
        }, modifier = Modifier.padding(8.dp)) {
            Text(text = "add")
        }
    }
}

@Composable
private fun TodoRow(state: TodoRow) {
    Row {
        Text(text = state.title, modifier = Modifier.padding(8.dp).fillMaxWidth())
    }
}

@Preview
@Composable
private fun TodoMainPreview() {
    MvStateTheme(darkTheme = false) {
        TodoMain(modifier = Modifier.padding(8.dp))
    }
}