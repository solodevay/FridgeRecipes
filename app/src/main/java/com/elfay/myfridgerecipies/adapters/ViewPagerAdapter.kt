package com.elfay.myfridgerecipies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elfay.myfridgerecipies.R
import com.elfay.myfridgerecipies.databinding.ItemViewPagerBinding

class ViewPagerAdapter(val list : List<String>) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager,parent,false)
        return ViewPagerViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val text = list[position]
        holder.itemView.textTes
    }

    override fun getItemCount(): Int {
     return list.size
    }
}