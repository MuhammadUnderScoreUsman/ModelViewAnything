package com.mohammadosman.modelviewanything.presentation.common.mvi

/** This is not ideal naming convention, it is used only for
 *  representation of different architectures.
 * **/
sealed class MviIntents {
    object LoadAllCommentsIntent : MviIntents()
    data class LoadUser(val userId: String) : MviIntents()
}