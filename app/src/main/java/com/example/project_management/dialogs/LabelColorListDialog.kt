package com.example.project_management.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_management.R
import com.example.project_management.databinding.DialogListBinding
import com.example.project_management.view.adapters.LabelColorListItemsAdapter

abstract class LabelColorListDialog(
    context: Context,
    private var list: ArrayList<String>,
    private val title: String = "",
    private var mSelectedColor: String = ""
) : Dialog(context) {

        private var adapter: LabelColorListItemsAdapter? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val binding = DialogListBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setCanceledOnTouchOutside(true)
            setCancelable(true)
            setUpRecyclerView(binding)
        }

        fun setSelectedColor(color: String) {
            mSelectedColor = color
            if (adapter != null) {
                adapter!!.notifyDataSetChanged()
            }
        }

        protected abstract fun onItemSelected(color: String)

        fun showDialog() {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_list)
            dialog.setCancelable(true)
            dialog.show()
        }

        private fun setUpRecyclerView(binding: DialogListBinding) {
            binding.tvTitle.text = title
            binding.rvList.layoutManager = LinearLayoutManager(context)
            adapter = LabelColorListItemsAdapter(context, list, mSelectedColor)
            binding.rvList.adapter = adapter

            adapter!!.onClickListener = object : LabelColorListItemsAdapter.OnClickListener {
                override fun onClick(position: Int, color: String) {
                    dismiss()
                    onItemSelected(color)
                }
            }
        }
}