# About

MvState is a simplify MVVM framework for android,

inspire by [Mavericks](https://github.com/airbnb/mavericks),[Redux](https://redux.js.org/),
[Android App Architecture](https://developer.android.com/topic/architecture)

# 核心概念

MvState 把功能分为3个部分: state， ui， logic

## state = kotlin immutable data class

大部分情况下，state 对应 ui 需要显示的内容, 例如 todo list 的 item， user profile 的 name，avatar，等等
```kotlin
data class TodoState(
    val id: Int,
    val title: String
)

data class VeryComplexUserState(
    val todo: Todo,
    val user: User,
    ... and more
)
```

类似 [UI State](https://developer.android.com/topic/architecture/ui-layer/stateholders)，
最大的区别是 MvState 仅仅是 immutable data class, 不能包含 任何逻辑，callback

## ui = ui layer + scope lifecycle + [optional ui state]

consume state, draw ui,
dispatch [UI Event](https://developer.android.com/topic/architecture/ui-layer/events) to logic.
may be some ui-layer-only state, like loading state, error state, if your ui is complex.
more about [Ui Layer](https://developer.android.com/topic/architecture/ui-layer)

## logic = data layer + [optional lifecycle]

logic 是逻辑，负责 更新 state, 也只有 logic 才可以修改 state。

see [Data Layer](https://developer.android.com/topic/architecture/data-layer)

### logic lifecycle
一般分 3 中，
[Ui-oriented](https://developer.android.com/topic/architecture/data-layer#ui-operations), 
[App-oriented](https://developer.android.com/topic/architecture/data-layer#app-operations)
[Business-oriented](https://developer.android.com/topic/architecture/data-layer#app-operations)

Ui-oriented Logic = lifecycle = ui lifecycle, destroy when ui is destroyed or detached. [](https://developer.android.com/topic/architecture/data-layer#ui-operations)

App-oriented Logic = lifecycle of app, 例如 全局的 App 逻辑，状态, 数据，等等，不需主动销毁，只有进程被kill 才会被动销毁

Business-oriented = 不需要跟随 ui 销毁，例如 当前登录用户的信息，只有用户退出登录的时候才需要销毁；
例如 提供用户信息的logic，不需要跟随 ui 销毁，作为 cache 存在，只有低内存的时候才需要销毁,


# Usage

add dependencies in build.gradle
```kotlin

dependencies {
    implementation 'xyz.icode:mvs:x.y.z'
}
```

# Demo

A Todo demo is in `app`