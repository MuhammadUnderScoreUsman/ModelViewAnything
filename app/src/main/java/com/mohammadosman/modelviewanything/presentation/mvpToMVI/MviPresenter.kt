package com.mohammadosman.modelviewanything.presentation.mvpToMVI

import com.mohammadosman.modelviewanything.presentation.common.mvi.MviIntents
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviIntents.LoadAllCommentsIntent
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviIntents.LoadUser
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviViewState
import com.mohammadosman.modelviewanything.usecase.mvi.MviUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

class MviPresenter(
    private var mviView: MviContract.IMviView? = null,
    private val mviUseCase: MviUseCase,
    private val dispatcher: CoroutineDispatcher
) : MviContract.IMviPresenter, CoroutineScope {

    private val exception =
        CoroutineExceptionHandler { _, e ->
            mviView?.viewState
                ?.invoke(MviViewState(uiResponse = e.localizedMessage))
        }

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatcher + job + exception


    override val launchIntents: (MviIntents) -> Unit
        get() = { intent ->
           val response =  when (intent) {
                LoadAllCommentsIntent -> {
                    mviUseCase.mviGetCommentList.getCommentList()
                }

                is LoadUser -> {
                    mviUseCase.mviGetUser.getUser(intent.userId)
                }
            }
            processResponse(response)
        }

    override fun processResponse(response: Flow<MviViewState>) {
            response.onEach { viewState ->
                mviView?.let { view ->
                    view.viewState(viewState)
                }
            }.launchIn(this)
    }

    override fun onDestroy() {
        this.cancel()
        mviView = null
    }

}