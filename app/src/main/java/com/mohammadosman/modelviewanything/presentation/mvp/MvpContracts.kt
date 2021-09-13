package com.mohammadosman.modelviewanything.presentation.mvp

import com.mohammadosman.modelviewanything.domain.Comment
import com.mohammadosman.modelviewanything.domain.User
import com.mohammadosman.modelviewanything.presentation.common.mvp.IBasePresenter
import com.mohammadosman.modelviewanything.presentation.common.mvp.IBaseView

interface MvpContracts {

    interface IMvpView : IBaseView<IMvpPresenter> {
        val progressBar: (Boolean) -> Unit
        val commentList: (List<Comment>) -> Unit
        val loadUser: (User) -> Unit
        val uiResponse: (String) -> Unit
    }

    interface IMvpPresenter : IBasePresenter {
        val fetchUser: (String) -> Unit
        val fetchComments: () -> Unit
        val fetchCommentsViaId: (String) -> Unit
    }

}