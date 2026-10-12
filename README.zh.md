[English](README.md) | **中文**

# android-kotlin-redux

https://github.com/airbnb/mavericks

https://redux.js.org/

并非 RxJava，基于 Kotlin Coroutines Flow 实现的精简 Redux。

完整介绍见博客：[Redux-for-Android](https://xechoz.github.io/2025/03/23/Redux-for-Android/)

## 基础概念

GUI 开发通常有 3 个角色：

1. **state**：描述数据状态，任意 immutable data class（需实现 `RxState` 标记接口）
2. **repo**：对应 Redux 的 action，负责存储、分发、更新 state
3. **view**：消费 state，触发 repo 方法

## 接入

```kotlin
implementation("io.github.xechoz:mvs:1.0.0")
```

minSdk 26，依赖 Kotlin Coroutines。

在本仓库内开发时改用 `implementation(project(":lib:mvs"))`。

## 使用 Demo

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

// 结合 ViewModel 使用
class FooViewModel : ViewModel(),
    RxRepo<FooState> by RxRepo(initState = FooState()) {

    fun updateFooToBar(bar: Any) {
        setState {
            copy(foo = bar)
        }
    }
}
```

## API 一览

| 成员 | 说明 |
| --- | --- |
| `val state: State` | 同步读取当前 state |
| `fun RxRepo<State>.setState(reducer: State.() -> State)` | 更新 state，reducer 以旧 state 为 receiver 返回新 state |
| `fun asFlow(): Flow<State>` | 将完整 state 作为 Flow 暴露 |
| `suspend fun onEach(action: (State) -> Unit)` | 监听完整 state |
| `suspend fun <A : Any> onEach(property: KProperty1<State, A>, action: (A) -> Unit)` | 监听单个属性变化 |
| `suspend fun <A : Any> onEach(property: KProperty1<State, A>): Flow<A>` | 将属性暴露为 Flow，便于 combine 组合 |
| `fun <State : RxState> RxRepo(initState: State): RxRepo<State>` | 创建 repo 的工厂函数 |

说明：

- `setState` 声明为 `RxRepo` 的扩展成员函数，只能在 repo 实现类或其扩展函数中调用，从而限制外部直接改 state。通过扩展函数为 repo 添加逻辑：

  ```kotlin
  fun TodoRepo.remove(todoRow: TodoRow) {
      setState {
          copy(todos = todos.filter { it != todoRow })
      }
  }
  ```

- `onEach` 的订阅生命周期交给调用方的协程作用域管理：在 `scope.launch { }` 中调用，`scope.cancel()` 即停止监听。
- 若 `onEach` 不能满足需求（如组合多个属性），使用 `asFlow()` 自行处理。

## 更多示例

- 本仓库 `app` 模块的 Todo 示例：[Todo.kt](app/src/main/java/xyz/icode/lib/todo/Todo.kt)、[Todo.Repo.kt](app/src/main/java/xyz/icode/lib/todo/Todo.Repo.kt)、[Todo.Ui.kt](app/src/main/java/xyz/icode/lib/todo/Todo.Ui.kt)
- 博客：[Redux-for-Android](https://xechoz.github.io/2025/03/23/Redux-for-Android/)
