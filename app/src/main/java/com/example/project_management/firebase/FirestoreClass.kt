package com.example.project_management.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.project_management.utils.Constants
import com.example.project_management.view.activity.CreateBoardActivity
import com.example.project_management.view.activity.MainActivity
import com.example.project_management.view.activity.MyProfileActivity
import com.example.project_management.view.activity.SignInActivity
import com.example.project_management.view.activity.SignUpActivity
import com.example.project_management.view.activity.TaskListActivity
import com.example.project_management.viewmodel.Board
import com.example.project_management.viewmodel.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while registering the user", e)
            }
    }

    fun getBoardDetails(activity: TaskListActivity, documentId: String) {
        mFireStore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val board = document.toObject(Board::class.java)!!
                // add board doc id to board
                board.documentId = document.id
                activity.boardDetails(board)
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }

    // A function to get the list of boards from the cloud firestore
    fun getBoardList(activity: MainActivity) {
        mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for (i in document.documents) {
                    val board = i.toObject(Board::class.java)!!
                    board.documentId = i.id
                    boardList.add(board)
                }
                activity.populateBoardListToUI(boardList)
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }

    // function to add(update) board's taskList field in board document
    fun addUpdateTaskList(activity: TaskListActivity, board: Board) {

        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "TaskList updated successfully!")

                activity.addUpdateTaskListSuccess()
            }.addOnFailureListener {
                exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board", exception)
            }
    }


    fun updateUserProfileData(
        activity: MyProfileActivity,
        userHashMap: HashMap<String, Any>
    ) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Profile Data updated successfully!")

                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                activity.profileUpdateSuccess()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board.",
                    e
                )
                Toast.makeText(activity, "Error when updating the profile!", Toast.LENGTH_LONG)
                    .show()
            }
    }



        fun loadUserData(activity: Activity, readBoardsList: Boolean = false) {

            mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .get()
                .addOnSuccessListener { document ->
                    val loggedInUser = document.toObject(User::class.java)!!
                    when (activity) {
                        is SignInActivity -> {
                            activity.signInSuccess(loggedInUser)
                        }

                        is MainActivity -> {
                            activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
                        }

                        is MyProfileActivity -> {
                            activity.setUserDataInUI(loggedInUser)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    when (activity) {
                        is SignInActivity -> {
                            activity.hideProgressDialog()
                        }

                        is MainActivity -> {
                            activity.hideProgressDialog()
                        }
                    }
                    Log.e(activity.javaClass.simpleName, "Error while registering the user", e)
                }
        }





    fun createBoard(activity: CreateBoardActivity, board: Board) {
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Board Create Successfully")
                Toast.makeText(activity, "Board Create Successfully", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener { exception ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board.",
                    exception
                )
            }
    }

    fun getCurrentUserId(): String {

        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        } else {
            Log.e("FireStoreClass", "User is null")
        }
        return currentUserId
    }
}
