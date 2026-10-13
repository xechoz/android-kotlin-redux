package xyz.icode.mvs

import kotlin.test.Test
import kotlin.test.assertEquals

private data class CounterState(val count: Int = 0) : RxState

private fun RxRepo<CounterState>.increment() {
    setState {
        copy(count = count + 1)
    }
}

class RxStateTest {
    @Test
    fun setStateUpdatesState() {
        val repo = RxRepo(CounterState())
        repo.increment()
        repo.increment()
        assertEquals(2, repo.state.count)
    }
}
