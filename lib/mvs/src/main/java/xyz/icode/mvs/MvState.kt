package xyz.icode.mvs

import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty1

interface MvState

// a state data class, observable properties

/**
 * update state, return new state
 */
interface StateProducer<T: MvState> {
    val state: StateFlow<T> // read only

    /**
     * 限制只能在 子类内部使用
     */
    fun StateProducer<T>.update(reducer:T.()->T)

    suspend fun<V> onUpdate(property: KProperty1<T, V>, observer: suspend (V) -> Unit)
}

fun <T: MvState> stateProducer(initState: T): StateProducer<T> = StateProducerImpl(initState)
