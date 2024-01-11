package com.example.project_management.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_management.R
import com.example.project_management.databinding.ItemMemberBinding
import com.example.project_management.utils.Constants
import com.example.project_management.viewmodel.User

class MemberListItemsAdapter (
    private val context: Context,
    private var list: ArrayList<User>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null


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

            if(model.selected) {
                holder.binding.ivSelectedMember.visibility = ViewGroup.VISIBLE
            } else {
                holder.binding.ivSelectedMember.visibility = ViewGroup.GONE
            }

            holder.itemView.setOnClickListener() {
                if(onClickListener != null) {
                    if(model.selected) {
                        onClickListener!!.onClick(position, model, Constants.UN_SELECT)
                    } else {
                        onClickListener!!.onClick(position, model, Constants.SELECT)
                    }
                }
            }

        }
    }

    /**
     * A function for OnClickListener where the Interface is the expected parameter..
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    /**
     * An interface for onclick items.
     */
    interface OnClickListener {
        fun onClick(position: Int, user: User, action: String)
    }

    class MyViewHolder(val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root)

}