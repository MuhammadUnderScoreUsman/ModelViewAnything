package com.mohammadosman.modelviewanything.presentation.common.mvi

import android.os.Parcelable
import com.mohammadosman.modelviewanything.domain.Comment
import com.mohammadosman.modelviewanything.domain.User
import kotlinx.parcelize.Parcelize


/** This is not ideal naming convention, it is used only for
 * representation of different architectures.
 * **/
@Parcelize
data class MviViewState(
    val loading: Boolean = false,
    val commentList: List<Comment>? = null,
    val user: User? = null,
    val uiResponse: String? = null
) : Parcelable