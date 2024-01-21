package com.example.passwordmanager.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.Data_Class.Website
import com.example.passwordmanager.R

class WebsiteAdapter : RecyclerView.Adapter<WebsiteAdapter.ViewHolder>(), Filterable {
    private var websiteList: List<Website> = emptyList()
    private var filteredWebsiteList: List<Website> = emptyList()

    init {
        setHasStableIds(true)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val urlTextView: TextView = itemView.findViewById(R.id.textViewUrl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.websitelist, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val website = filteredWebsiteList[position]
        holder.nameTextView.text = website.name
        holder.urlTextView.text = website.url
    }

    override fun getItemCount(): Int = filteredWebsiteList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setData(data: List<Website>) {
        websiteList = data
        filteredWebsiteList = data
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val queryString = constraint?.toString()?.toLowerCase()

                filteredWebsiteList = if (queryString.isNullOrBlank()) {
                    websiteList
                } else {
                    websiteList.filter { it.name.toLowerCase().contains(queryString) }
                }

                filterResults.values = filteredWebsiteList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredWebsiteList = results?.values as? List<Website> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}
