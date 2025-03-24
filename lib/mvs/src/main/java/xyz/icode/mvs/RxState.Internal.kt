package xyz.icode.mvs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

internal
class RxRepoImpl<State : RxState>(initState: State) : RxRepo<State> {
    private val stateFlow = MutableStateFlow(initState)

    override val state: State
        get() = stateFlow.value

    override fun asFlow(): Flow<State> = stateFlow.asStateFlow()

    @Suppress("UNCHECKED_CAST")
    override fun RxRepo<State>.setState(reducer: State.() -> State) {
        stateFlow.value = reducer(state)
    }

    override suspend fun <A : Any> onEach(
        property: KProperty1<State, A>,
        action: (A) -> Unit
    ) {
        childScope().launch {
            stateFlow.collectLatest {
                action(property.get(it))
            }
        }
    }

    override suspend fun onEach(action: (State) -> Unit) {
        childScope().launch {
            stateFlow.collectLatest(action)
        }
    }

    override suspend fun <A : Any> onEach(property: KProperty1<State, A>): Flow<A> {
        val scope = childScope()

        return flow {
            scope.launch {
                stateFlow
                    .collectLatest {
                        emit(property.get(it))
                    }
            }
        }
    }

    private suspend inline fun childScope(): CoroutineScope {
        return CoroutineScope(context = currentCoroutineContext())
    }
}