package com.example.project_management.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_management.R
import com.example.project_management.databinding.ItemTaskBinding
import com.example.project_management.view.activity.TaskListActivity
import com.example.project_management.viewmodel.Task

open class TaskListItemsAdapter(
    private val context: Context,
    // taskList or list
    private var list: ArrayList<Task>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // viewholder should be 0.7 of the parent width
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(
            (15.toDp().toPx()), 0, (40.toDp()).toPx(), 0)

        binding.root.layoutParams = layoutParams

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        val model = list[position]
        if(holder is MyViewHolder) {
            if(position == list.size - 1) {
                holder.binding.tvAddTaskList.visibility = View.VISIBLE
                holder.binding.llTaskItem.visibility = View.GONE
            } else {
                holder.binding.tvAddTaskList.visibility = View.GONE
                holder.binding.llTaskItem.visibility = View.VISIBLE
            }

            holder.binding.tvTaskListTitle.text = model.title

            holder.binding.tvAddTaskList.setOnClickListener {
                holder.binding.tvAddTaskList.visibility = View.GONE
                holder.binding.cvAddTaskListName.visibility = View.VISIBLE
            }

            holder.binding.ibCloseListName.setOnClickListener {
                holder.binding.tvAddTaskList.visibility = View.VISIBLE
                holder.binding.cvAddTaskListName.visibility = View.GONE
            }

            // the check button next to input field
            holder.binding.ibDoneListName.setOnClickListener {
                val listName = holder.binding.etTaskListName.text.toString()

                if(listName.isNotEmpty()) {
                    if(context is TaskListActivity) {
                        context.createTaskList(listName)
                    }
                } else {
                    Toast.makeText(context, "Please Enter List Name.", Toast.LENGTH_SHORT).show()
                }
            }

            //================ Editing list name =====================//
            holder.binding.ibEditListName.setOnClickListener {
                holder.binding.etEditTaskListName.setText(model.title)
                holder.binding.llTitleView.visibility = View.GONE
                holder.binding.cvEditTaskListName.visibility = View.VISIBLE
            }

            holder.binding.ibCloseEditableView.setOnClickListener {
                holder.binding.llTitleView.visibility = View.VISIBLE
                holder.binding.cvEditTaskListName.visibility = View.GONE
            }

            holder.binding.ibDoneEditListName.setOnClickListener {
                val listName = holder.binding.etEditTaskListName.text.toString()

                if(listName.isNotEmpty()) {
                    if(context is TaskListActivity) {
                        context.updateTaskList(position, listName, model)
                    }
                } else {
                    Toast.makeText(context, "Please Enter a List Name.", Toast.LENGTH_SHORT).show()
                }

            }

            //================ Deleting list (taskList) =====================//
            holder.binding.ibDeleteList.setOnClickListener {
                alertDialogForDeleteList(position, model.title)
            }


            //======================= Card =============================//
            holder.binding.tvAddCard.setOnClickListener {
                holder.binding.tvAddCard.visibility = View.GONE
                holder.binding.cvAddCard.visibility = View.VISIBLE
            }

            holder.binding.ibCloseCardName.setOnClickListener {
                holder.binding.tvAddCard.visibility = View.VISIBLE
                holder.binding.cvAddCard.visibility = View.GONE
            }

            //================ Create Card ==============//
            holder.binding.ibDoneCardName.setOnClickListener {
                val cardName = holder.binding.etCardName.text.toString()

                if(cardName.isNotEmpty()) {
                    if(context is TaskListActivity) {
                        context.addCardToTaskList(position, cardName)
                    }
                } else {
                    Toast.makeText(context, "Please Enter List Name.", Toast.LENGTH_SHORT).show()
                }
            }

            // load cards inside each taskList
            holder.binding.rvCardList.layoutManager = LinearLayoutManager(context)
            // adjusting size
            holder.binding.rvCardList.setHasFixedSize(true)


            val adapter = CardListItemsAdapter(context, model.cards)
            holder.binding.rvCardList.adapter = adapter


        }
    }

    /**
     * Method is used to show the Alert Dialog for deleting the task list.
     */
    private fun alertDialogForDeleteList(position: Int, title: String) {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete $title.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed

            if (context is TaskListActivity) {
                context.deleteTaskList(position)
            }
        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
    // END



    // get dp from px
    private fun Int.toDp(): Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()


    // get px from dp
    private fun Int.toPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()


    class MyViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root)
}