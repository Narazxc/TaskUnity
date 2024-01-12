package com.example.project_management.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_management.R
import com.example.project_management.databinding.ItemCardSelectedMemberBinding
import com.example.project_management.model.SelectedMembers

class CardMemberListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<SelectedMembers>
) : RecyclerView.Adapter<CardMemberListItemsAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardSelectedMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        if (position == list.size - 1) {
            holder.binding.ivAddMember.visibility = ViewGroup.VISIBLE
            holder.binding.ivSelectedMemberImage.visibility = ViewGroup.GONE
        } else {
            holder.binding.ivAddMember.visibility = ViewGroup.GONE
            holder.binding.ivSelectedMemberImage.visibility = ViewGroup.VISIBLE

            Glide
                .with(context)
                .load(model.image)
                .centerCrop().placeholder(R.drawable.ic_user_place_holder)
                .into(holder.binding.ivSelectedMemberImage)
        }

        holder.itemView.setOnClickListener() {
            if (onClickListener != null) {
                onClickListener!!.onClick()
            }
        }

    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick()
    }

    class MyViewHolder(val binding: ItemCardSelectedMemberBinding) : RecyclerView.ViewHolder(binding.root)
}
