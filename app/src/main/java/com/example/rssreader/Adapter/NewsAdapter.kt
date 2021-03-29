package com.example.rssreader.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rssreader.Interface.ItemClickListener
import com.example.rssreader.R
import com.example.rssreader.models.Json4Kotlin_Base

class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
    var textTitle: TextView = itemView.findViewById(R.id.textTitle)
    var textPubDate: TextView = itemView.findViewById(R.id.textPubDate)
    var textContent: TextView = itemView.findViewById(R.id.textContent)
    private lateinit var itemClickListener: ItemClickListener
    init {
        itemView.setOnClickListener(this)
    }
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    override fun onClick(v: View?) {
        itemClickListener.onItemClick(v,absoluteAdapterPosition,false)
    }
}
class NewsAdapter(private val newsObject: Json4Kotlin_Base, private val mContext: Context): RecyclerView.Adapter<NewsViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(mContext)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = inflater.inflate(R.layout.row, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.textTitle.text = newsObject.response.results[position].sectionName
        holder.textContent.text = newsObject.response.results[position].webTitle
        holder.textPubDate.text = newsObject.response.results[position].webPublicationDate
        holder.setItemClickListener(ItemClickListener { view, position, isLongClick ->
            if (!isLongClick){
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsObject.response.results[position].webUrl))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                mContext.startActivity(intent)
            }
        })
    }

    override fun getItemCount(): Int {
        return newsObject.response.results.size
    }

}