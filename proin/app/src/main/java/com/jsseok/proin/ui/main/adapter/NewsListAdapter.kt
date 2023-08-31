package com.jsseok.proin.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jsseok.proin.R
import com.jsseok.proin.data.model.News
import com.jsseok.proin.databinding.RecyclerViewItemBinding
import com.squareup.picasso.Picasso

// NewsListAdapter : RecycleView 어댑터 - EngNews와 MainNews 두 곳에 사용
class NewsListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val newsList = mutableListOf<News>()    // Data
    private lateinit var itemClickListener: OnItemClickListener

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        val binding = RecyclerViewItemBinding.bind(view)
        return Holder(binding)
    }

    // 생성된 뷰 홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as Holder
        mHolder.bind(newsList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, newsList, position)
        }
        holder.apply {
            bind(newsList[position])
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, newNewsList: List<News>, position: Int) {}
    }

    fun setItemClickListenr(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun updateNews(newNewsList: List<News>) {
        this.newsList.clear()
        this.newsList.addAll(newNewsList)
        notifyDataSetChanged()
    }

    // 할당
    inner class Holder(private val binding: RecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            binding.newsTitle.text = news.title
            binding.newsDetail.text = news.description
            Picasso.get().load(news.urlToImage).into(binding.newsImage)
        }
    }
}