package com.example.project_management.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_management.R
import androidx.appcompat.widget.Toolbar

class CreateBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)
        setupActionBar()
    }

    private fun setupActionBar(){
        val toolbar: Toolbar = findViewById(R.id.toolbar_create_board_activity)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionbar.title = resources.getString(R.string.create_board_title)
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed() }
    }
}