package xyz.icode.mvs

/**
 * Module 对应 一个业务逻辑，
 * Module = UI + State + Logic
 * 
 * 一个 大的 Module 可以包含 多个 Module， 例如 
 *  LoginModule = GoogleLoginModule + AppleLoginModule
 *  GoogleLoginModule = GoogleLoginUI + GoogleLoginState + GoogleLoginLogic
 *  AppleLoginModule = AppleLoginUI + AppleLoginState + AppleLoginLogic
 */

interface MvStateModule {
}