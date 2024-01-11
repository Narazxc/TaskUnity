package com.example.project_management.view.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.project_management.R
import com.example.project_management.databinding.ActivityCardDetailsBinding
import com.example.project_management.databinding.ActivityMembersBinding
import com.example.project_management.firebase.FirestoreClass
import com.example.project_management.utils.Constants
import com.example.project_management.viewmodel.Board
import com.example.project_management.viewmodel.Card

class CardDetailsActivity : BaseActivity() {

    lateinit var binding: ActivityCardDetailsBinding
    private lateinit var mBoardDetails : Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_card_details)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        setupActionBar()

        binding.etNameCardDetails.setText(mBoardDetails
            .taskList[mTaskListPosition]
            .cards[mCardPosition].name)

        binding.etNameCardDetails.setSelection(binding
            .etNameCardDetails.text.toString().length)

        binding.btnUpdateCardDetails.setOnClickListener {
            if (binding.etNameCardDetails.text.toString().isNotEmpty()) {
                updateCardDetails()
            } else {
                showErrorSnackBar("Please enter a card name.")
            }
        }
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

    fun addUpdateTaskListSuccess() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.action_delete_card -> {
                alertDialogForDeleteCard(mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getIntentData() {
        if (intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }
        if (intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)){
            mTaskListPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)){
            mCardPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION, -1)
        }
    }

    private fun updateCardDetails() {
        val card = Card(
            binding.etNameCardDetails.text.toString(),
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].createdBy,
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo
        )

        mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition] = card

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@CardDetailsActivity, mBoardDetails)
    }

    private fun deleteCard(){
        val cardsList: ArrayList<Card> = mBoardDetails.taskList[mTaskListPosition].cards
        cardsList.removeAt(mCardPosition)

        val taskList: ArrayList<com.example.project_management.viewmodel.Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size - 1)

        taskList[mTaskListPosition].cards = cardsList

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@CardDetailsActivity, mBoardDetails)
    }

    private fun alertDialogForDeleteCard(cardName: String){
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.alert))
        builder.setMessage(
            resources.getString(
                R.string.confirmation_message_to_delete_card,
                cardName
            )
        )
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, which ->
            dialogInterface.dismiss()
            deleteCard()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: android.app.AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}