package xyz.icode.mvs

import xyz.icode.mvs.internal.MvLogicImpl
import kotlin.reflect.KProperty1

interface MvState

// a state data class, observable properties

/**
 * Logic of [MvState], is like a ViewModel of MVVM
 *
 * manage state update and dispatch updates to observers of [onAny]
 * use [onAny] to observe state or state's property changes
 */
interface MvLogic<T : MvState> {
    val state: T // read only

    /**
     * Restricted to be used only within subclasses
     */
    suspend fun MvLogic<T>.update(reducer: suspend T.() -> T)

    /**
     * property change will trigger [onUpdate]
     * usage: fooProducer.onAny(FooState::bar) { ... }
     */
    suspend fun <V> onAny(property: KProperty1<T, V>, onUpdate: suspend (V) -> Unit)

    /**
     * any property change will trigger [onUpdate]
     * usage: fooProducer.onAny(FooState::bar, FooState::baz) { a, b -> ... }
     */
    suspend fun <A, B> onAny(
        property: KProperty1<T, A>,
        property2: KProperty1<T, B>,
        onUpdate: suspend (A, B) -> Unit
    )

    /**
     * any property change will trigger [onUpdate]
     * usage: fooProducer.onAny(FooState::bar, FooState::baz, FooState::qux) { a, b, c -> ... }
     */
    suspend fun <A, B, C> onAny(
        property: KProperty1<T, A>,
        property2: KProperty1<T, B>,
        property3: KProperty1<T, C>,
        onUpdate: suspend (A, B, C) -> Unit
    )

    /**
     * any property of [state] changed will trigger [onUpdate]
     * usage: fooProducer.onAny { state -> ... }
     */
    suspend fun onAny(onUpdate: suspend (T) -> Unit)
}

fun <T : MvState> stateProducer(initState: T): MvLogic<T> = MvLogicImpl(initState)
