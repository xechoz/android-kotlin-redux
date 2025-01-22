package xyz.icode.mvs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

data class AMvState(val name: String): MvState


class AStore: StateProducer<AMvState> by stateProducer(AMvState("")) {

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
            aStore.onUpdate(AMvState::name) {

            }
        }

        scope.launch {
            aStore.onUpdate(AMvState::name, ::onNameChange)
        }
    }

    private fun onNameChange(name: String) {

    }
}

data class BMvState(val b: String): MvState

class BStore: StateProducer<BMvState> by stateProducer(BMvState("")) {
    private val aStore = AStore()

    fun updateB(b: String) {
        update {
            copy(b = b)
        }
    }
}
