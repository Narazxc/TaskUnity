package com.example.project_management.view.activity

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_management.R
import com.example.project_management.adapters.MemberListItemsAdapter
import com.example.project_management.databinding.ActivityMembersBinding
import com.example.project_management.firebase.FirestoreClass
import com.example.project_management.utils.Constants
import com.example.project_management.viewmodel.Board
import com.example.project_management.viewmodel.User


class MembersActivity : BaseActivity() {

    lateinit var binding: ActivityMembersBinding

    lateinit var mBoardDetails: Board


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init view binding
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.BOARD_DETAIL)) {
            mBoardDetails = intent.getParcelableExtra<Board>(Constants.BOARD_DETAIL)!!
        }

        setupActionBar()

        // should be no error since there is always one assignedTo member (the user/board creator)
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
    }

    // let this activity knows it should use MemberListItemsAdapter
    fun setUpMembersList(list: ArrayList<User>) {
        hideProgressDialog()

        // set layout manager
        binding.rvMembersList.layoutManager = LinearLayoutManager(this)
        binding.rvMembersList.setHasFixedSize(true)

        val adapter = MemberListItemsAdapter(this, list)

        // assign adapter to recyclerView
        binding.rvMembersList.adapter = adapter
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarMembersActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.members)
        }

        binding.toolbarMembersActivity.setNavigationOnClickListener { onBackPressed() }
    }

    // whenever you add menu, need to create two function
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_member, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_member -> {

                dialogSearchMember()

                // return true because function expect Boolean
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogSearchMember() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_member)

        // Find views within the dialog_search_member layout
        val tvAdd = dialog.findViewById<TextView>(R.id.tv_add)
        val tvCancel = dialog.findViewById<TextView>(R.id.tv_cancel)
        val etEmailSearchMember = dialog.findViewById<AppCompatEditText>(R.id.et_email_search_member)


        tvAdd.setOnClickListener {
            val email = etEmailSearchMember.text.toString()

            if(email.isNotEmpty()) {
                dialog.dismiss()
                // TODO implement adding member logic
            } else {
                Toast.makeText(
                    this@MembersActivity,
                    "Please enter members email address",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        tvCancel.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }
}