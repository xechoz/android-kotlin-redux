package xyz.icode.mvs

import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

/**
 * Module 对应 一个业务逻辑，
 * Module = UI + State + Logic
 * 
 * 一个 大的 Module 可以包含 多个 Module， 例如 
 *  LoginModule = GoogleLoginModule + AppleLoginModule
 *  GoogleLoginModule = GoogleLoginUI + GoogleLoginState + GoogleLoginLogic
 *  AppleLoginModule = AppleLoginUI + AppleLoginState + AppleLoginLogic
 */

data class MvStateModule(
    val scope : CoroutineScope,
)

fun<S: MvState, T: MvLogic<S>> MvStateModule.logicOf(clazz: KClass<T>): T = TODO()