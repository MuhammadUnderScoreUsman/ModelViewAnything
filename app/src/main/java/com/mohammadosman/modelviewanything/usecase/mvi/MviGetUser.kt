package com.mohammadosman.modelviewanything.usecase.mvi

import com.mohammadosman.modelviewanything.framework.ApiService
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


/** This is bad(even worst) naming convention of class(use case)
 * it is only being used because of the purpose of this project sample
 * about different architectures
 * */
class MviGetUser(
    private val apiService: ApiService
) {

    fun getUser(userId: String): Flow<MviViewState> {
        return flow {
            emit(MviViewState(loading = true))

            if (userId.isEmpty() || userId == "") {
                emit(MviViewState(uiResponse = "Please input something valid..."))
                return@flow
            }
            val response = apiService.getUser(userId.toInt())
            emit(MviViewState(user = response))
        }.catch { e ->
            emit(MviViewState(uiResponse = e.localizedMessage ?: "unknown error"))
        }
    }
}