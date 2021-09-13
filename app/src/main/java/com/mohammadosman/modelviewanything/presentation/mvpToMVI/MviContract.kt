package com.mohammadosman.modelviewanything.presentation.mvpToMVI

import com.mohammadosman.modelviewanything.presentation.common.mvi.MviIntents
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviViewState
import com.mohammadosman.modelviewanything.presentation.common.mvp.IBasePresenter
import com.mohammadosman.modelviewanything.presentation.common.mvp.IBaseView
import kotlinx.coroutines.flow.Flow

interface MviContract {

    interface IMviView : IBaseView<IMviPresenter> {
        val viewState: (MviViewState) -> Unit
    }

    interface IMviPresenter : IBasePresenter {
        val launchIntents: (MviIntents) -> Unit
        fun processResponse(response: Flow<MviViewState>)
    }

}