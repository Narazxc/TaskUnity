package com.example.project_management.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.project_management.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_management.view.adapters.BoardItemAdapter
import com.example.project_management.utils.Constants
import com.example.project_management.firebase.FirestoreClass
import com.example.project_management.viewmodel.Board
import com.example.project_management.viewmodel.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object{
        const val MY_PROFILE_REQUEST_CODE: Int = 11
        const val CREATE_BOARD_REQUEST_CODE: Int = 12
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var mUserName: String
    private lateinit var rvBoardsList: RecyclerView
    private lateinit var tvNoBoardsAvailable: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvBoardsList = findViewById(R.id.rv_boards_list)
        tvNoBoardsAvailable = findViewById(R.id.tv_no_boards_available)

        val fabButton: FloatingActionButton = findViewById(R.id.fab_create_board)
        fabButton.setOnClickListener {

            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)

        }

        setUpActionBar()

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)


        FirestoreClass().loadUserData(this@MainActivity, true)
    }

    fun populateBoardListToUI(boardList: ArrayList<Board>) {

        hideProgressDialog()

        if (boardList.size > 0) {
            findViewById<TextView>(R.id.tv_no_boards_available).visibility = android.view.View.GONE
            findViewById<RecyclerView>(R.id.rv_boards_list).visibility =
                android.view.View.VISIBLE

            val rvBoardList = findViewById<RecyclerView>(R.id.rv_boards_list)
            rvBoardList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MainActivity)
            rvBoardList.setHasFixedSize(true)

            val adapter = BoardItemAdapter(this@MainActivity, boardList)
            rvBoardList.adapter = adapter

            adapter.setOnClickListener(object : BoardItemAdapter.OnClickListener {
                override fun onClick(position: Int, model: com.example.project_management.viewmodel.Board) {
                    val intent = Intent(this@MainActivity, TaskListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                    startActivity(intent)
                }
            })
        } else{
            findViewById<TextView>(R.id.tv_no_boards_available).visibility = android.view.View.VISIBLE
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_boards_list).visibility =
                android.view.View.GONE
        }
    }

    fun populateBoardsListToUI(boardsList: ArrayList<Board>) {
        hideProgressDialog()

        if (boardsList.size > 0){
            rvBoardsList.visibility = View.VISIBLE
            tvNoBoardsAvailable.visibility = View.GONE

            rvBoardsList.layoutManager = LinearLayoutManager(this)
            rvBoardsList.setHasFixedSize(true)

            val adapter = BoardItemAdapter(this, boardsList)
            rvBoardsList.adapter = adapter
        } else {
            rvBoardsList.visibility = View.GONE
            tvNoBoardsAvailable.visibility = View.VISIBLE
        }
    }

    private fun setUpActionBar() {
        toolbar = findViewById(R.id.toolbar_main_activity)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolbar.setNavigationOnClickListener {
            // Toggle drawer
            toggleDrawer()
        }
    }

    fun updateNavigationUserDetails(user: User, readBoardsList: Boolean) {
        mUserName = user.name
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val navUserImage: ImageView = headerView.findViewById(R.id.nav_user_image)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navUserImage)

        val navUsername = headerView.findViewById<TextView>(R.id.tv_username)
        navUsername.text = user.name
        if (readBoardsList){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getBoardsList(this)
        }
    }

    private fun toggleDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( resultCode == RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE ){
            FirestoreClass().loadUserData(this)
        } else if ( resultCode == Activity.RESULT_OK && requestCode == CREATE_BOARD_REQUEST_CODE ) {
            FirestoreClass().getBoardList(this)
        } else {
//            Toast.makeText(this, "Profile update failed", Toast.LENGTH_LONG).show()
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_my_profile ->{
                // Toast.makeText(this@MainActivity, "My Profile", Toast.LENGTH_LONG).show()
                startActivityForResult(Intent(this@MainActivity, MyProfileActivity::class.java), MY_PROFILE_REQUEST_CODE)
            }

            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
