package com.mohammadosman.modelviewanything.presentation.mvvmToMVI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammadosman.modelviewanything.domain.Comment
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviIntents
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviViewState
import com.mohammadosman.modelviewanything.usecase.mvi.MviUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MViewModel(
    private val mviUseCase: MviUseCase,
) : ViewModel() {


    private val _viewState = MutableLiveData<MviViewState>()
    val viewState: LiveData<MviViewState>
        get() = _viewState


    // for uiResponse
    private val _uiResponse = Channel<String>()
    val uiResponse = _uiResponse.receiveAsFlow()


    internal fun process(intent: MviIntents) {
        val response = when (intent) {
            is MviIntents.LoadUser -> {
                mviUseCase.mviGetUser.getUser(intent.userId)
            }

            MviIntents.LoadAllCommentsIntent -> {
                mviUseCase.mviGetCommentList.getCommentList()
            }
        }
        processResponse(response)
    }

    private val processResponse: (Flow<MviViewState>) -> Unit
        get() = { viewState ->
            viewState.onEach {
                incomingResponse(it)
            }.launchIn(viewModelScope)
        }


    private val incomingResponse: (MviViewState) -> Unit
    get() = {
        viewState ->
        updateLoading(viewState.loading)
        viewState.commentList?.let {
            // this is utility function which reduces the state
            reducer(MviResult.CommentList(it))
        }

        viewState.user?.let {
            reducer(MviResult.User(it))
        }

        /** use some proper utility class to handle effects,
         * this is just simple variation to utilize the kotlin Channels
         * for one time events
        */
        viewState.uiResponse?.let {
            updateUiResponse(it)
        }

    }

    private fun updateLoading(loading: Boolean) {
        val reducer: MviViewState = initViewState().copy(loading = loading)
        updateViewState(reducer)
    }

    private fun updateUiResponse(msg: String) {
        viewModelScope.launch {
            _uiResponse.send(msg)
        }
    }


    internal fun updateViewState(state: MviViewState) {
        this._viewState.value = state
    }


    private fun initViewState(): MviViewState {
        return viewState.value ?: MviViewState()
    }

    /**
     * Important role of this function here,
     * reducer basically takes the "previous state and gets the result and gives the
     * new state"
    * */
    private fun reducer(
        mviResult: MviResult
    ): MviViewState {
        /**
         * this `reduce` is previous state of the `viewState`
         * */
        var reduce = initViewState()
            reduce = when (mviResult) {

                /**
                 * we are getting the result and give the newState
                 * to the previousState, containing the "commentList" and
                 * also updating the loading to false
                 * */
                is MviResult.CommentList -> {
                    reduce.copy(
                        loading = false,
                        commentList = mviResult.comments
                    )
                }
                /** same goes over */
                is MviResult.User -> {
                    reduce.copy(
                        loading = false,
                        user = mviResult.user
                    )
                }
            }

        /**
         * making sure to update the view immediately as soon as we are getting the
         * updated viewState, we are using liveData for observing the view
         * */
        updateViewState(reduce)
        return reduce
    }

    sealed class MviResult {
        data class CommentList(val comments: List<Comment>) : MviResult()
        data class User(val user: com.mohammadosman.modelviewanything.domain.User) : MviResult()
    }

}