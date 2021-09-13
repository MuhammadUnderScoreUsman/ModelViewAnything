package com.mohammadosman.modelviewanything.presentation.mvp

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohammadosman.modelviewanything.databinding.ActivityMvpBinding
import com.mohammadosman.modelviewanything.domain.Comment
import com.mohammadosman.modelviewanything.domain.User
import com.mohammadosman.modelviewanything.framework.ApiInstance
import com.mohammadosman.modelviewanything.presentation.common.CommentAdapter
import com.mohammadosman.modelviewanything.presentation.common.toast
import com.mohammadosman.modelviewanything.usecase.GetCommentList
import com.mohammadosman.modelviewanything.usecase.GetUser
import kotlinx.coroutines.Dispatchers

class MvpActivity : AppCompatActivity(), MvpContracts.IMvpView {

    companion object {
        const val USER_KEY = "USER_KEY"
        const val COMMENT_LIST_KEY = "COMMENT_LIST_KEY"
    }

    private var binding: ActivityMvpBinding? = null
    private var presenter: MvpContracts.IMvpPresenter? = null
    private val apiInstance = ApiInstance.instance
    private var commentAdapter: CommentAdapter? = null

    private var user: User? = null
    private var commentLst: List<Comment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initResView()
        setupPresenter(
            MvpPresenter(
                dispatcher = Dispatchers.Main,
                mvpView = this,
                getCommentList = GetCommentList(apiInstance),
                getUser = GetUser(apiInstance)

            )
        )
        binding?.apply {
            btnFetch.setOnClickListener {
                presenter?.apply {
                    fetchUser(ediTxtUserInput.text.toString())
                    fetchComments()
                }

            }
        }

    }

    override val setupPresenter: (MvpContracts.IMvpPresenter) -> Unit
        get() = { presenter ->
            this.presenter = presenter
        }

    override val progressBar: (Boolean) -> Unit
        get() = {
            progressBar(it)
        }

    override val commentList: (List<Comment>) -> Unit
        get() = {
            commentAdapter?.submitList(it)
            commentLst = it
        }


    override val loadUser: (User) -> Unit
        get() = {
            binding?.txtUser?.text = it.name
            user = it

        }

    override val uiResponse: (String) -> Unit
        get() = {
            toast(it)
        }

    private fun progressBar(isVisible: Boolean) {
        binding?.progressBar?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun initResView() {
        binding?.commentResView?.apply {
            layoutManager = LinearLayoutManager(
                this@MvpActivity
            )
            commentAdapter = CommentAdapter()
            adapter = commentAdapter

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putParcelableArrayList(COMMENT_LIST_KEY, commentLst as ArrayList<out Parcelable>)
            putParcelable(USER_KEY, user)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.apply {
            getParcelableArrayList<Comment>(COMMENT_LIST_KEY)?.let { lst ->
                commentList(lst)
            }
            getParcelable<User>(USER_KEY)?.let { user ->
                loadUser(user)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
        commentAdapter = null
        binding = null
    }
}