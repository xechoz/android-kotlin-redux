**English** | [中文](README.zh.md)

# android-kotlin-redux

https://github.com/airbnb/mavericks

https://redux.js.org/

Not RxJava — a minimal Redux built on Kotlin Coroutines Flow.

Read the full introduction (Chinese): [Redux-for-Android](https://xechoz.github.io/2025/03/23/Redux-for-Android/)

## Concepts

A GUI app usually has 3 roles:

1. **state**: describes the data, any immutable data class (must implement the `RxState` marker interface)
2. **repo**: the Redux equivalent of an action — stores, dispatches and updates state
3. **view**: consumes state and triggers repo methods

## Setup

```kotlin
implementation("io.github.xechoz:mvs:1.0.0")
```

minSdk 26, depends on Kotlin Coroutines.

For development inside this repository, use `implementation(project(":lib:mvs"))` instead.

## Usage Demo

```kotlin
// state
data class CounterState(val count: Int = 0) : RxState

// repo (= action)
class CounterRepo : RxRepo<CounterState> by RxRepo(initState = CounterState()) {
    fun increment() {
        setState {
            copy(count = count + 1)
        }
    }
}

// view
class CounterView {
    private val counter = CounterRepo()
    private val scope = MainScope()

    init {
        scope.launch {
            counter.onEach(CounterState::count) {
                text = "Click $it"
            }
        }

        setOnClickListener {
            counter.increment()
        }
    }
}

// with ViewModel
class FooViewModel : ViewModel(),
    RxRepo<FooState> by RxRepo(initState = FooState()) {

    fun updateFooToBar(bar: Any) {
        setState {
            copy(foo = bar)
        }
    }
}
```

## API

| Member | Description |
| --- | --- |
| `val state: State` | Synchronously read the current state |
| `fun RxRepo<State>.setState(reducer: State.() -> State)` | Update state; the reducer receives the old state as receiver and returns the new state |
| `fun asFlow(): Flow<State>` | Expose the whole state as a Flow |
| `suspend fun onEach(action: (State) -> Unit)` | Observe the whole state |
| `suspend fun <A : Any> onEach(property: KProperty1<State, A>, action: (A) -> Unit)` | Observe a single property |
| `suspend fun <A : Any> onEach(property: KProperty1<State, A>): Flow<A>` | Expose a property as a Flow, ready to be combined |
| `fun <State : RxState> RxRepo(initState: State): RxRepo<State>` | Factory function to create a repo |

Notes:

- `setState` is declared as an extension member of `RxRepo`, so it can only be called from a repo implementation or its extension functions, preventing external mutation. Add logic to a repo via extension functions:

  ```kotlin
  fun TodoRepo.remove(todoRow: TodoRow) {
      setState {
          copy(todos = todos.filter { it != todoRow })
      }
  }
  ```

- The subscription lifetime of `onEach` is managed by the caller's coroutine scope: call it inside `scope.launch { }`; `scope.cancel()` stops observing.
- If `onEach` is not enough (e.g. combining multiple properties), use `asFlow()` instead.

## More examples

- The Todo sample in the `app` module: [Todo.kt](app/src/main/java/xyz/icode/lib/todo/Todo.kt), [Todo.Repo.kt](app/src/main/java/xyz/icode/lib/todo/Todo.Repo.kt), [Todo.Ui.kt](app/src/main/java/xyz/icode/lib/todo/Todo.Ui.kt)
- Blog (Chinese): [Redux-for-Android](https://xechoz.github.io/2025/03/23/Redux-for-Android/)
