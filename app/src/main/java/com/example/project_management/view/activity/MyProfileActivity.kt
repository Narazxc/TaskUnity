package com.example.project_management.view.activity

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.project_management.R
import com.example.project_management.databinding.ActivityMyProfileBinding
import com.example.project_management.firebase.FirestoreClass
import com.example.project_management.utils.Constants
import com.example.project_management.viewmodel.User

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

// need to change AppCompatActivity to BaseActivity
class MyProfileActivity : BaseActivity() {

    // define view binding
    // using view binding will automatically convert snake case naming to camel case
    lateinit var binding: ActivityMyProfileBinding


    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserDetails: User
    private var mProfileImageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init view binding
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        FirestoreClass().loadUserData(this)


        // permission to access external storage for image chooser
        binding.ivProfileUserImage.setOnClickListener{
            if(ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

                Constants.showImageChooser(this)

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding.btnUpdate.setOnClickListener {
            if(mSelectedImageFileUri != null) {
                uploadUserImage()
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))

                // Call a function to update user details in the database.
                updateUserProfileData()
            }
        }
    }

    // catch permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constants.showImageChooser(this)
            }
        } else {
            Toast.makeText(this, "Oops, you just denied the permission for storage. You can allow it from settings.",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    // do something after getting the activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null) {

            // data.data returns Uri
            mSelectedImageFileUri = data.data

            try {
                // load image for preview
                Glide
                    .with(this@MyProfileActivity)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(binding.ivProfileUserImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarMyProfileActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }

        binding.toolbarMyProfileActivity.setNavigationOnClickListener { onBackPressed() }
    }


    fun setUserDataInUI(user: User){

        mUserDetails = user

        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(findViewById(R.id.iv_profile_user_image))

        findViewById<TextView>(R.id.et_name).text = user.name
        findViewById<TextView>(R.id.et_email).text = user.email
        if (user.mobile != 0L){
            findViewById<TextView>(R.id.et_mobile).text = user.mobile.toString()
        }
    }

    private fun updateUserProfileData( ) {
        // create user hashmap
        val userHashMap = HashMap<String, Any>()

        // Log the name and mobile before updating the user profile data
        val newName = binding.etName.text.toString()
        val newMobile = binding.etMobile.text.toString()
        // Use Log.i to log an informational message
        Log.i("UserProfileUpdate", "Name: $newName, Mobile: $newMobile")

        // add img url, name, mobile number to hashmap
        // and check if nothing changes, if nothing changes don't do anything
        if(mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashMap[Constants.IMAGE] = mProfileImageURL
        }


        if(binding.etName.text.toString() != mUserDetails.name) {
            userHashMap[Constants.NAME] = binding.etName.text.toString()
        }

        if (binding.etMobile.text.toString() != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = binding.etMobile.text.toString().toLong()
        }

            FirestoreClass().updateUserProfileData(this, userHashMap)

    }




    private fun uploadUserImage() {
        showProgressDialog(resources.getString(R.string.please_wait))

        if(mSelectedImageFileUri != null) {

            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis()
                        + "." + Constants.getFileExtension(this, mSelectedImageFileUri))

                // upload image to firebase storage
                sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                    taskSnapShot ->
                    Log.i(
                        "Firebase Image URL",
                        taskSnapShot.metadata!!.reference!!.downloadUrl.toString()
                    )

                    taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri ->
                        Log.i("Downloadable Image URL", uri.toString())
                        // assign the uri we got from firebase to mProfileImageURL global variable
                        mProfileImageURL = uri.toString()

                        // updateUserProfileData inside of uploadUserImage function
                        updateUserProfileData()

                    }
                }.addOnFailureListener {
                    exception ->
                    Toast.makeText(this@MyProfileActivity,
                    exception.message,
                    Toast.LENGTH_LONG
                    ).show()

                    hideProgressDialog()
                }

        }
    }



    fun profileUpdateSuccess() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
}