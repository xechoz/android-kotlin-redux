package xyz.icode.mvs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

internal class StateProducerImpl<T : MvState>(initState: T) : StateProducer<T> {
    private val stateFlow = MutableStateFlow(initState)

    override val state: T get() = stateFlow.value

    override suspend fun StateProducer<T>.update(reducer: suspend T.() -> T) {
        val to = reducer(stateFlow.value)
        stateFlow.value = to
    }

    override suspend fun <V> onAny(
        property: KProperty1<T, V>, onUpdate: suspend (V) -> Unit
    ) = launch {
        stateFlow.map(property::get).collectLatest(onUpdate)
    }

    override suspend fun <A, B> onAny(
        property: KProperty1<T, A>, property2: KProperty1<T, B>, onUpdated: suspend (A, B) -> Unit
    ) = launch {
        stateFlow.map {
            Pair(property.get(it), property2.get(it))
        }.collectLatest { (a, b) -> onUpdated(a, b) }
    }

    override suspend fun <A, B, C> onAny(
        property: KProperty1<T, A>,
        property2: KProperty1<T, B>,
        property3: KProperty1<T, C>,
        onUpdated: suspend (A, B, C) -> Unit
    ) = launch {
        stateFlow.map {
            Triple(property.get(it), property2.get(it), property3.get(it))
        }.collectLatest { (a, b, c) -> onUpdated(a, b, c) }
    }

    override suspend fun onAny(onUpdated: suspend (T) -> Unit) {
        launch {
            stateFlow.collectLatest(onUpdated)
        }
    }

    private suspend inline fun launch(noinline block: suspend CoroutineScope.() -> Unit) {
        CoroutineScope(context = currentCoroutineContext()).launch(block = block)
    }
}