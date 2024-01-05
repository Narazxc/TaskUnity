package com.example.project_management.view.activity

import android.app.Activity
import android.content.Intent
import android.widget.EditText
import com.bumptech.glide.Glide
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import android.os.Bundle
import com.example.project_management.R
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log
import android.widget.Button
import com.example.project_management.firebase.FirestoreClass
import com.example.project_management.utils.Constants
import com.example.project_management.viewmodel.Board
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class CreateBoardActivity : BaseActivity() {

    private var mSelectedImageFileUri : Uri? = null
    private lateinit var mUserName: String
    private var mBoardImageURL: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board);
        setupActionBar()

        if (intent.hasExtra(Constants.NAME)){
            mUserName = intent.getStringExtra(Constants.NAME) ?: "HelloWorld"
        }

        val iv_board_image: CircleImageView = findViewById(R.id.iv_board_image)
        iv_board_image.setOnClickListener{ view ->
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        val btn_create: Button = findViewById(R.id.btn_create)
        btn_create.setOnClickListener {
            if (mSelectedImageFileUri != null){
                uploadBoardImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                createBoard()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createBoard(){
        val assignedUsersArrayList: ArrayList<String> = ArrayList()
        assignedUsersArrayList.add(getCurrentUserID())

        val et_board_name: AppCompatEditText = findViewById(R.id.et_board_name)
        var board = Board(
            et_board_name.text.toString(),
            mBoardImageURL,
            mUserName,
            assignedUsersArrayList
        )

        FirestoreClass().createBoard(this, board)
    }

    private fun uploadBoardImage(){
        showProgressDialog(resources.getString(R.string.please_wait))

        val sRef: StorageReference =
            FirebaseStorage.getInstance().reference.child(
            "BOARD_IMAGE" + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(this, mSelectedImageFileUri)
        )

        //adding the file to reference
        sRef.putFile(mSelectedImageFileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Board Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        // assign the image url to the variable.
                        mBoardImageURL = uri.toString()

                        // Call a function to update user details in the database.
                        createBoard()
                    }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    exception.message,
                    Toast.LENGTH_LONG
                ).show()

                hideProgressDialog()
            }
    }

    fun boardCreatedSuccessfully(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val iv_board_image: CircleImageView = findViewById(R.id.iv_board_image)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null) {
            mSelectedImageFileUri = data.data

            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_board_place_holder)
                    .into(iv_board_image)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}