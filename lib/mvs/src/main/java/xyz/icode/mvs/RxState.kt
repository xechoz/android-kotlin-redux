package xyz.icode.mvs

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KProperty1

// empty marker interface
interface RxState

interface RxRepo<State : RxState> {
    // read only
    val state: State

    /*
    * 这个写法，可以限制 只能在 Repo 的 实现类，或者 扩张方法 才可以 改数据
    * 如果 需要更 严格的约束，可以改成吧 setState 放到 base class 的 protect 方法
    * 但是 我不想这样做。这样会导致 无法使用 kotlin 的扩展函数.
    * 通过 扩展 添加 功能，是我非常喜欢的一个特性，必须支持
    *
    * 例如 给 FooRepo 扩展 新的逻辑:
    * fun FooRepo.updateStateForBar(bar) {
    *   setState {
    *       copy(bar)
    *   }
    * }
    */
    fun RxRepo<State>.setState(reducer: State.() -> State)

    /**
     * flow 可以提供更灵活的使用。如果 onEach 不能满足，就用这个
     */
    fun asFlow(): Flow<State>

    /**
     * listen State.foo change
     *
     * usage:
     * fooRepo.onEach(FooState::bar) {
     * }
     */
    suspend fun <A : Any> onEach(property: KProperty1<State, A>, action: (A) -> Unit)

    /**
     * 用于 组合 监听 多个 property, 或者 需要 flow 实现更复杂的功能
     * usage:
     *
     * combine(fooRepo.onEach(FooState::a), fooRepo.onEach(FooState::b), fooRepo.onEach(FooState::c), ::Triple)
     * .collectLatest { (a,b,c) ->
     *
     * }
     *
     * fooRepo.onEach(FooState::a).map {
     *  "a is $it"
     * }.collect {
     *
     * }
     */
    suspend fun <A : Any> onEach(property: KProperty1<State, A>): Flow<A>


    suspend fun onEach(action: (State) -> Unit)
}

fun <State: RxState> RxRepo(initState: State): RxRepo<State> = RxRepoImpl(initState)