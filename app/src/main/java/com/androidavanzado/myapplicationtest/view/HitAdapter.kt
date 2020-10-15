package com.androidavanzado.myapplicationtest.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidavanzado.myapplicationtest.R
import com.androidavanzado.myapplicationtest.model.Hit
import kotlinx.android.synthetic.main.list_row.view.*

class HitAdapter(private val myDataset: ArrayList<Hit>, private val cellClickListener: CellClickListener) :

    RecyclerView.Adapter<HitAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTv : TextView = itemView.titleTv
        val authorTv : TextView = itemView.authorTv
        val createdAtTv : TextView = itemView.createdAtTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return MyViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.titleTv.text = myDataset[position].story_title
        holder.authorTv.text = myDataset[position].author
        holder.createdAtTv.text = myDataset[position].created_at
        holder.itemView.setOnClickListener{
            cellClickListener.onCellClickListener(myDataset[position].story_url)
        }
    }

    override fun getItemCount() = myDataset.size
}