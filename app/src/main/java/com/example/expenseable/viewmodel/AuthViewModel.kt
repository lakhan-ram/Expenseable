package com.example.expenseable.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseable.model.repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(context: Context): ViewModel() {
    private val authRepository = AuthRepository(context)
    val authList: MutableLiveData<FirebaseUser> = authRepository.authList
    val signOut: MutableLiveData<Boolean> = authRepository.signOut

    fun signUp(name: String, email: String, password: String) {
        authRepository.signUp(name, email, password)
    }

    fun signIn(email:String, password:String){
        authRepository.signIn(email, password)
    }

    fun resetPassword(email: String){
        authRepository.resetPassword(email)
    }

    fun signOut(){
        authRepository.signOut()
    }
}