package com.example.project_management.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_management.R
import com.example.project_management.databinding.ItemMemberBinding
import com.example.project_management.viewmodel.User

class MemberListItemsAdapter (
    private val context: Context,
    private var list: ArrayList<User>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder) {

            Glide
                .with(context)
                .load(model.image)
                .centerCrop().placeholder(R.drawable.ic_user_place_holder)
                .into(holder.binding.ivMemberImage)

            holder.binding.tvMemberName.text = model.name
            holder.binding.tvMemberEmail.text = model.email

        }
    }

    class MyViewHolder(val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root)



}