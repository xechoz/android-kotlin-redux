package xyz.icode.lib.todo

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "TodoMain"

@Composable
fun TodoMain(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val model = remember { TodoViewModel() }
    val todoRows = remember { mutableStateOf(emptyList<TodoRow>()) }

    LaunchedEffect(key1 = todoRows) {
        scope.launch {
            model.onUpdate(TodoState::todos) {
                Log.d(TAG, "onUpdate: $it")
                todoRows.value = it
            }
        }

        scope.launch {
            delay(100)
            model.addTodo("b")
        }
    }

    Column(modifier = modifier) {
        TodoAdder(model)

        todoRows.value.forEach { TodoRow(it) }
    }
}

@Composable
fun TodoAdder(model: TodoViewModel) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = {
            model.addTodo("New Todo ${model.state.value.todos.size}")
        }, modifier = Modifier.padding(8.dp)) {
            Text(text = "add")
        }
    }
}

@Composable
fun TodoRow(state: TodoRow) {
    Row {
        Text(text = state.title, modifier = Modifier.padding(8.dp).fillMaxWidth())
    }
}

@Preview
@Composable
fun TodoMainPreview() {
    TodoMain(modifier = Modifier.padding(8.dp))
}