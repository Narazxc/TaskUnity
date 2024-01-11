package com.example.project_management.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_management.R
import com.example.project_management.databinding.ItemBoardBinding
import com.example.project_management.viewmodel.Board

class BoardItemAdapter(
    private val context: Context,
    private var list: ArrayList<Board>
) : RecyclerView.Adapter<BoardItemAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model)
    }

    interface OnClickListener{
        fun onClick(position: Int, model: Board)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    inner class MyViewHolder(private val binding: ItemBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Board) {
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(binding.ivBoardImage)

            binding.tvName.text = model.name
            binding.tvCreatedBy.text = "Created by: ${model.createdBy}"

            binding.root.setOnClickListener {
                if (onClickListener != null){
                    onClickListener!!.onClick(position, model)
//                    Toast.makeText(context, "Board Clicked", Toast.LENGTH_SHORT).show ()
                }
            }
        }
    }
}
