package org.geeksforgeeks.demo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practicletask.ListModel
import com.example.practicletask.R

class ListAdapter(var context: Context, var list: MutableList<ListModel>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]


        holder.textView.text = item.notes

    }

    fun updateList(newList: MutableList<ListModel>) {
        list = newList
        notifyDataSetChanged() // A simple way to refresh the entire list
        // For better performance, use DiffUtil.
    }
    fun filterList(filterList: MutableList<ListModel>) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }
    fun addItem(item: ListModel, position: Int = list.size) {
        list.add(position, item)
        notifyItemInserted(position)
        // Optionally, use notifyItemRangeChanged() if adding in the middle affects other items
    }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = itemView.findViewById(R.id.text1)
    }
}