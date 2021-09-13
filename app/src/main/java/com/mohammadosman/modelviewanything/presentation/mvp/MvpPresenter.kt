package com.mohammadosman.modelviewanything.presentation.mvp

import com.mohammadosman.modelviewanything.usecase.GetCommentList
import com.mohammadosman.modelviewanything.usecase.GetUser
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MvpPresenter(
    private val dispatcher: CoroutineDispatcher,
    private var mvpView: MvpContracts.IMvpView?,
    private val getCommentList: GetCommentList,
    private val getUser: GetUser
) : MvpContracts.IMvpPresenter, CoroutineScope {

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        mvpView?.apply {
            uiResponse(e.localizedMessage ?: "unknownError")
            progressBar(false)
        }
    }

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatcher + job + exceptionHandler


    override val fetchUser: (String) -> Unit
        get() = { userId ->
            mvpView?.let {
                launch {
                    if (userId.isEmpty() || userId == "") {
                        it.uiResponse("Please input something...")
                        return@launch
                    }
                    it.progressBar(true)
                    val user = getUser.getUser(userId = userId.toInt())
                    it.loadUser(user)
                    it.progressBar(false)
                }
            }

        }

    override val fetchComments: () -> Unit
        get() = {
            mvpView?.let {
                launch {
                    it.progressBar(true)
                    try {
                        val commentList =
                            getCommentList.getCommentList()
                        it.commentList(commentList)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    it.progressBar(false)
                }
            }
        }

    override val fetchCommentsViaId: (String) -> Unit
        get() = { id ->
            mvpView?.let {
                launch {
                    if (id.isEmpty() || id == "") {
                        it.uiResponse("Please input something...")
                        return@launch
                    }
                    it.progressBar(true)
                    val commentList =
                        getCommentList.getCommentListViaId(id.toInt())
                    it.commentList(commentList)
                    it.progressBar(false)
                }
            }
        }

    override fun onDestroy() {
        this.cancel()
        mvpView = null
    }
}