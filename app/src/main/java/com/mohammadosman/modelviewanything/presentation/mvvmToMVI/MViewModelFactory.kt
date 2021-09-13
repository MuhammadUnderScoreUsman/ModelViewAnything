package com.mohammadosman.modelviewanything.presentation.mvvmToMVI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohammadosman.modelviewanything.usecase.mvi.MviUseCase

class MViewModelFactory(
    private val mviUseCase: MviUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MViewModel::class.java)) {
            return MViewModel(mviUseCase) as T
        }
        throw IllegalAccessException("unknown view model")
    }
}