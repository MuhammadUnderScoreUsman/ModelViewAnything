package com.mohammadosman.modelviewanything.usecase.mvi

import com.mohammadosman.modelviewanything.framework.ApiService
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/** This is bad(even worst) naming convention of class(use case)
 * it is only being used because of Purpose of this project sample
 * about different architectures
 * */
class MviGetCommentList(
    private val apiService: ApiService
) {

    fun getCommentList(): Flow<MviViewState> {
        return flow {
            emit(MviViewState(loading = true))
            val response = apiService.getAllComments()
            if(response.isEmpty()){
                emit(MviViewState(uiResponse = "empty list..."))
                return@flow
            }
            emit(MviViewState(commentList = response))
        }.catch {e ->
            emit(MviViewState(uiResponse = e.localizedMessage ?: "unknown error"))
        }
    }

}