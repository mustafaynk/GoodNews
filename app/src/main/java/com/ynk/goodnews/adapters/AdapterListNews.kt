package com.ynk.goodnews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ynk.goodnews.R
import com.ynk.goodnews.clicklisteners.AdapterItemClickListener
import com.ynk.goodnews.databinding.NewsBinding
import com.ynk.goodnews.model.News

class AdapterListNews(
    private val items: List<News>,
    private val adapterItemClickListener: AdapterItemClickListener
) : RecyclerView.Adapter<AdapterListNews.NewsViewHolder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder2 {
        val newsBinding: NewsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_news_dashboard,
            parent,
            false
        )
        return NewsViewHolder2(newsBinding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder2, position: Int) {
        holder.bind(getItem(position), adapterItemClickListener)
    }

    override fun getItemCount() = items.size

    private fun getItem(position: Int) = items[position]

    inner class NewsViewHolder2(newsBinding: NewsBinding) : RecyclerView.ViewHolder(newsBinding.root) {
        private var newsBinding: NewsBinding = newsBinding

        fun bind(news: News?, adapterItemClickListener: AdapterItemClickListener?) {
            newsBinding.news = news
            newsBinding.clickListener = adapterItemClickListener
        }
    }
}