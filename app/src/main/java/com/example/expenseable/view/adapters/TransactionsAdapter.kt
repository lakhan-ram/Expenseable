package com.example.expenseable.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseable.R
import com.example.expenseable.model.entities.Transaction

class TransactionsAdapter: RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    var transactionList: List<Transaction> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_each_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.tvTransactionTitle.text = transaction.title
        holder.tvTransactionDate.text = transaction.date
        if (transaction.type == "Income") {
            holder.tvTransactionAmount.text = "+$ ${transaction.amount}"
        } else {
            holder.tvTransactionAmount.text = "-$ ${transaction.amount}"
        }

    }

    class TransactionViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvTransactionTitle: TextView = view.findViewById(R.id.tvTransactionTitle)
        val tvTransactionDate: TextView = view.findViewById(R.id.tvTransactionDate)
        val tvTransactionAmount: TextView = view.findViewById(R.id.tvTransactionAmount)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Transaction>) {
        transactionList = list
        notifyDataSetChanged()
    }
}