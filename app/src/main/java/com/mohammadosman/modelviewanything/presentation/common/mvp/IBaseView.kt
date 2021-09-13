package com.mohammadosman.modelviewanything.presentation.common.mvp

interface IBaseView<T> {
    val setupPresenter: (T) -> Unit
}