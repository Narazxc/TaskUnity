package com.example.project_management.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_management.R
import com.example.project_management.databinding.ActivityCardDetailsBinding
import com.example.project_management.databinding.ActivityMembersBinding
import com.example.project_management.utils.Constants
import com.example.project_management.viewmodel.Board

class CardDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityCardDetailsBinding
    private lateinit var mBoardDetails : Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)
        getIntentData()
        setupActionBar()

    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCardDetailsActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name
        }
        binding.toolbarCardDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getIntentData() {
        val extras = intent.extras
        if (extras != null) {
            mBoardDetails = extras.getParcelable(Constants.BOARD_DETAIL) ?: Board()
            mTaskListPosition = extras.getInt(Constants.TASK_LIST_ITEM_POSITION, -1)
            mCardPosition = extras.getInt(Constants.CARD_LIST_ITEM_POSITION, -1)
        }
    }
}