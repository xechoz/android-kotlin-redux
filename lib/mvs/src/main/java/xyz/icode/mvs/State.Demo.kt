package xyz.icode.mvs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

data class AState(val name: String): State


class AStore: StateProducer<AState> by stateProducer(AState("")) {

    fun updateName(name: String) {
        update {
            copy(name = name)
        }
    }
}

class AView {
    private val scope: CoroutineScope = MainScope()

    private val aStore = AStore()
    private val bStore = BStore()

    fun read() {
        aStore.state.value

//        bStore.state.value = BState("123")

        bStore.updateB(b = "456")
    }

    fun write() {
        aStore.updateName(name = "123")

        // collect property
        scope.launch {
            aStore.onUpdate(AState::name) {

            }
        }

        scope.launch {
            aStore.onUpdate(AState::name, ::onNameChange)
        }
    }

    private fun onNameChange(name: String) {

    }
}

data class BState(val b: String): State

class BStore: StateProducer<BState> by stateProducer(BState("")) {
    private val aStore = AStore()

    fun updateB(b: String) {
        update {
            copy(b = b)
        }
    }
}
