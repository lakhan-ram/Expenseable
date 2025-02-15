package com.example.expenseable.model.repositories

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expenseable.model.entities.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TransactionRepository(private val context: Context) {
    private val firebaseReference = FirebaseDatabase.getInstance().getReference("transactions")
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    fun addTransaction(expense: Transaction) {
        firebaseReference.child(expense.id).setValue(expense)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Transaction added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error: ${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun getTransactions(): LiveData<List<Transaction>> {
        val transactionList = MutableLiveData<List<Transaction>>()
        firebaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Transaction>()
                for (data in snapshot.children) {
                    if (data.child("userId").value == userId) {
                        val id = data.child("id").value.toString()
                        val title = data.child("title").value.toString()
                        val amount = data.child("amount").value.toString()
                        val type = data.child("type").value.toString()
                        val date = data.child("date").value.toString()
                        val userId = data.child("userId").value.toString()
                        val transaction = Transaction(id, title, amount, type, date, userId)
                        list.add(transaction)
                    }
                }
                transactionList.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
        return transactionList
    }

    fun deleteTransaction(id: String) {
        firebaseReference.child(id).removeValue()
    }
}