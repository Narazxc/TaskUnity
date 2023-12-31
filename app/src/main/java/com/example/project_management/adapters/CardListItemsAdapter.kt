package com.example.project_management.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_management.databinding.ItemCardBinding
import com.example.project_management.viewmodel.Board
import com.example.project_management.viewmodel.Card


class CardListItemsAdapter (
    private val context: Context,
    private var list: ArrayList<Card>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        val model = list[position]

        if(holder is MyViewHolder) {
            holder.binding.tvCardName.text = model.name

        }
    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Board)
    }




    class MyViewHolder(val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root)
    }