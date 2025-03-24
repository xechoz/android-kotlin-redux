package xyz.icode.lib.todo


internal
fun TodoRepo.remove(todoRow: TodoRow) {
    setState {
        copy(todos = todos.filter { it != todoRow })
    }
}