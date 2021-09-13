package com.mohammadosman.modelviewanything.presentation.mvpToMVI

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohammadosman.modelviewanything.databinding.ActivityMvpToMviBinding
import com.mohammadosman.modelviewanything.framework.ApiInstance
import com.mohammadosman.modelviewanything.presentation.common.CommentAdapter
import com.mohammadosman.modelviewanything.presentation.common.TAG
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviIntents
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviViewState
import com.mohammadosman.modelviewanything.presentation.common.toast
import com.mohammadosman.modelviewanything.usecase.mvi.MviGetCommentList
import com.mohammadosman.modelviewanything.usecase.mvi.MviGetUser
import com.mohammadosman.modelviewanything.usecase.mvi.MviUseCase
import kotlinx.coroutines.Dispatchers

class MvpToMviActivity : AppCompatActivity(), MviContract.IMviView {

    private var binding: ActivityMvpToMviBinding? = null
    private lateinit var presenter: MviContract.IMviPresenter
    private val apiInstance = ApiInstance.instance
    private var commentAdapter: CommentAdapter? = null
    private var vState: MviViewState? = null

    companion object {
        const val VS_KEY = "VS_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvpToMviBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initResView()
        setupPresenter(
            MviPresenter(
                dispatcher = Dispatchers.Main,
                mviUseCase = MviUseCase(
                    mviGetCommentList = MviGetCommentList(apiInstance),
                    mviGetUser = MviGetUser(apiInstance)
                ),
                mviView = this
            )
        )

        binding?.apply {
            btnFetch.setOnClickListener {
                presenter.apply {
                    launchIntents(
                        MviIntents.LoadUser(
                            ediTxtUserInput.text.toString()
                        )
                    )
                    launchIntents(
                        MviIntents.LoadAllCommentsIntent
                    )
                }
            }
        }
    }


    override val setupPresenter: (MviContract.IMviPresenter) -> Unit
        get() = {
            this.presenter = it
        }


    override val viewState: (MviViewState) -> Unit
        get() = { viewState ->
            vState = viewState
            viewState.uiResponse?.let {
                toast(it)
            }
            viewState.commentList?.let {
                commentAdapter?.submitList(it)
            }
            viewState.user?.let {
                binding?.txtUser?.text = it.username
            }
            progressBar(viewState.loading)
        }

    private fun progressBar(isVisible: Boolean) {
        binding?.progressBar?.visibility =
            if (isVisible) View.VISIBLE else View.GONE
    }

    private fun initResView() {
        binding?.commentResView?.apply {
            layoutManager = LinearLayoutManager(
                this@MvpToMviActivity
            )
            commentAdapter = CommentAdapter()
            adapter = commentAdapter

        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(VS_KEY, vState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getParcelable<MviViewState>(VS_KEY)?.let { state ->
            viewState(state)
            Log.d(TAG, "${state.user}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        commentAdapter = null
        binding = null
    }
}