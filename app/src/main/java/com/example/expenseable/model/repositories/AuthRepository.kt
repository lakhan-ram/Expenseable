package com.example.expenseable.model.repositories

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.expenseable.model.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class AuthRepository(private val context: Context) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseReference = FirebaseDatabase.getInstance().getReference("users")
    val authList: MutableLiveData<FirebaseUser> = MutableLiveData<FirebaseUser>()

    init {
        if (firebaseAuth.currentUser != null) {
            authList.postValue(firebaseAuth.currentUser)
        }
    }

    fun signUp(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { taskSignUp ->
                if (taskSignUp.isSuccessful) {
                    val id = firebaseAuth.currentUser?.uid.toString()
                    val user = User(id, name, email)
                    firebaseReference.child(id).setValue(user)
                        .addOnCompleteListener { taskCreateUser ->
                            if (taskCreateUser.isSuccessful) {
                                authList.postValue(firebaseAuth.currentUser)
                            } else {
                                firebaseAuth.currentUser?.delete()
                                Toast.makeText(
                                    context,
                                    "Error: ${taskCreateUser.exception?.localizedMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        context,
                        "Error: ${taskSignUp.exception?.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun signIn(email:String, password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { taskSignIn ->
                if (taskSignIn.isSuccessful) {
                    authList.postValue(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        context,
                        "Error: ${taskSignIn.exception?.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun resetPassword(email:String){
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { taskReset ->
                if (taskReset.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Email sent",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}