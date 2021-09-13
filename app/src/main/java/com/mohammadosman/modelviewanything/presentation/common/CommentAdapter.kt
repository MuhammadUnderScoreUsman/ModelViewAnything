package com.mohammadosman.modelviewanything.presentation.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mohammadosman.modelviewanything.R
import com.mohammadosman.modelviewanything.domain.Comment


class CommentAdapter : RecyclerView.Adapter<CommentAdapter.MainViewHolder>() {


    private val comparator =
        object : DiffUtil.ItemCallback<Comment>() {

            override fun areItemsTheSame(oldItem: Comment, newItem: Comment)
                    : Boolean {
                return oldItem.email == newItem.email
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment)
                    : Boolean {
                return oldItem.email == newItem.email
            }
        }

    private val differ =
        AsyncListDiffer(this, comparator)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comment_main,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        when (holder) {
            is MainViewHolder -> {
                val itm = differ.currentList[position]
                holder.bind(itm)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(lst: List<Comment>) {
        differ.submitList(lst)
    }

    inner class MainViewHolder(
        private val itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val commentX: TextView = itemView.findViewById(R.id.txtView_comment)
        val idX: TextView = itemView.findViewById(R.id.txtView_id)

        fun bind(comment: Comment) {
            commentX.text = comment.email
            idX.text = "${comment.id}: "
        }
    }


}