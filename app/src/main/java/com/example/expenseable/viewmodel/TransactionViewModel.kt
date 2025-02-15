package com.example.expenseable.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.expenseable.model.entities.Transaction
import com.example.expenseable.model.repositories.TransactionRepository

class TransactionViewModel(context: Context): ViewModel() {
    private val transactionRepository = TransactionRepository(context)
    val transactionList: LiveData<List<Transaction>> = transactionRepository.getTransactions()

    fun addTransaction(expense: Transaction) {
        transactionRepository.addTransaction(expense)
    }

    fun deleteTransaction(id: String) {
        transactionRepository.deleteTransaction(id)
    }
}