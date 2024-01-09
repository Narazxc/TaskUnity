package com.example.project_management.view.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_management.R
import com.example.project_management.databinding.ItemLabelColorBinding

class LabelColorListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<String>,
    private val mSelectedColor: String,
) : RecyclerView.Adapter<LabelColorListItemsAdapter.MyViewHolder>() {

    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Use ViewBinding to inflate the layout
        val binding = ItemLabelColorBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        val binding = holder.binding

        binding.viewMain.setBackgroundColor(Color.parseColor(item))

        if (item == mSelectedColor) {
            binding.ivSelectedColor.visibility = View.VISIBLE
        } else {
            binding.ivSelectedColor.visibility = View.GONE
        }

        binding.root.setOnClickListener {
            onClickListener?.onClick(position, item)
        }
    }

    // Update MyViewHolder to take the generated ViewBinding class
    class MyViewHolder(val binding: ItemLabelColorBinding) : RecyclerView.ViewHolder(binding.root)

    // Rest of your adapter code...

    interface OnClickListener {
        fun onClick(position: Int, color: String)
    }
}
