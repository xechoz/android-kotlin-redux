package xyz.icode.mvs

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlin.reflect.KProperty1


internal class StateStoreImpl<T: State>(initState: T) : StateProducer<T> {
    private val stateFlow = MutableStateFlow(initState)

    override val state: StateFlow<T> = stateFlow.asStateFlow()

    override fun StateProducer<T>.update(reducer: T.() -> T) {
        val to = reducer(stateFlow.value)
        stateFlow.value = to
    }

    override suspend fun <V> onUpdate(
        property: KProperty1<T, V>,
        observer: suspend (V) -> Unit
    ) {
        state.map(property::get).collectLatest(observer)
    }
}