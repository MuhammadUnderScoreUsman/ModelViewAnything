package com.mohammadosman.modelviewanything.presentation.mvvmToMVI

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohammadosman.modelviewanything.databinding.ActivityMvvmToMviBinding
import com.mohammadosman.modelviewanything.framework.ApiInstance
import com.mohammadosman.modelviewanything.presentation.common.CommentAdapter
import com.mohammadosman.modelviewanything.presentation.common.logd
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviIntents
import com.mohammadosman.modelviewanything.presentation.common.mvi.MviViewState
import com.mohammadosman.modelviewanything.presentation.common.toast
import com.mohammadosman.modelviewanything.usecase.mvi.MviGetCommentList
import com.mohammadosman.modelviewanything.usecase.mvi.MviGetUser
import com.mohammadosman.modelviewanything.usecase.mvi.MviUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MvvmToMviActivity : AppCompatActivity() {

    companion object {
        const val VS_KEY = "VS_KEY"
    }


    private var binding: ActivityMvvmToMviBinding? = null
    private val apiInstance = ApiInstance.instance
    private var commentAdapter: CommentAdapter? = null

    private val viewmodelFactory = MViewModelFactory(
        mviUseCase = MviUseCase(
            mviGetCommentList = MviGetCommentList(apiInstance),
            mviGetUser = MviGetUser(apiInstance)
        ),
    )

    private val viewmodel by viewModels<MViewModel> {
        viewmodelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvvmToMviBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initResView()
        initObserver()
        binding?.apply {
            btnFetch.setOnClickListener {
                viewmodel.apply {
                    process(
                        intent =
                        MviIntents.LoadUser(ediTxtUserInput.text.toString())
                    )
                    process(intent = MviIntents.LoadAllCommentsIntent)
                }
            }
        }
    }

    private fun initObserver() {
        viewmodel.viewState.observe(this, {
            it?.let { viewState ->
                progressBar(viewState.loading)
                viewState.commentList?.let {
                    commentAdapter?.submitList(it)
                }
                viewState.user?.let {
                    binding?.apply {
                        txtUser.text = it.username
                    }
                }
            }
        })
        lifecycleScope.launch {
            viewmodel.uiResponse.collect {
                toast(it)
            }
        }
    }

    private fun progressBar(isVisible: Boolean) {
        binding?.progressBar?.visibility =
            if (isVisible) View.VISIBLE else View.GONE
    }

    private fun initResView() {
        binding?.commentResView?.apply {
            layoutManager = LinearLayoutManager(
                this@MvvmToMviActivity
            )
            commentAdapter = CommentAdapter()
            adapter = commentAdapter

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val vs = viewmodel.viewState.value
        outState.putParcelable(VS_KEY, vs)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getParcelable<MviViewState>(VS_KEY)?.let {
            viewmodel.updateViewState(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        commentAdapter = null
        binding = null
    }
}