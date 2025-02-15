package com.example.expenseable.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseable.R
import com.example.expenseable.databinding.ActivityMainBinding
import com.example.expenseable.model.entities.Transaction
import com.example.expenseable.view.adapters.TransactionsAdapter
import com.example.expenseable.viewmodel.AuthViewModel
import com.example.expenseable.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TransactionViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var adapter: TransactionsAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var authViewModel: AuthViewModel
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()
        viewModel = TransactionViewModel(this)
        authViewModel = AuthViewModel(this)

        authViewModel.signOut.observe(this) {
            if (it) {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.child(firebaseAuth.currentUser?.uid.toString()).child("name").get()
            .addOnSuccessListener {
                binding.tvUserName.text = "Hello ${it.value.toString()}"
            }


        binding.btnSignOut.setOnClickListener {
            authViewModel.signOut()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransactionsAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.transactionList.observe(this) { list ->
            if (list != null) {
                binding.progressBarMain.visibility = View.GONE
                if (list.isEmpty()) {
                    binding.tvNoTransactions.visibility = View.VISIBLE
                    binding.progressBarMain.visibility = View.GONE
                } else {
                    binding.tvNoTransactions.visibility = View.GONE
                    binding.tvIncome.text = "$ ${list.filter { it.type == "Income" }.sumOf { it.amount.toDouble() }}"
                    binding.tvExpense.text = "$ ${list.filter { it.type == "Expense" }.sumOf { it.amount.toDouble() }}"
                    binding.tvTotalBalance.text = "$ ${list.filter { it.type == "Income" }.sumOf { it.amount.toDouble() } - list.filter { it.type == "Expense" }.sumOf { it.amount.toDouble() }}"
                    adapter.updateList(list)
                }
            }
        }

        binding.btnAdd.setOnClickListener {
            openDialog()
        }

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = adapter.transactionList[viewHolder.adapterPosition].id
                viewModel.deleteTransaction(id)
            }

        }).attachToRecyclerView(binding.recyclerView)
    }

    @SuppressLint("SimpleDateFormat")
    private fun openDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.create_transaction_dialog, null)
        val tvTransactionTitle = view.findViewById<EditText>(R.id.etTextTransactionTitle)
        val tvTransactionAmount = view.findViewById<EditText>(R.id.etTextTransactionAmount)
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Income", "Expense"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        btnSave.setOnClickListener {
            val transactionTitle = tvTransactionTitle.text.toString()
            val transactionAmount = tvTransactionAmount.text.toString()
            val transactionType = spinner.selectedItem.toString()
            val id = System.currentTimeMillis().toString()
            val date = SimpleDateFormat("dd MMMM yyyy").format(Date(System.currentTimeMillis())).toString()
            val userId = firebaseAuth.currentUser?.uid.toString()
            if (transactionTitle.isNotEmpty() && transactionAmount.isNotEmpty() && transactionType.isNotEmpty()) {
                viewModel.addTransaction(Transaction(id, transactionTitle, transactionAmount, transactionType, date, userId))
                dialog.dismiss()
            } else {
                tvTransactionTitle.error = "Please enter a title"
                tvTransactionAmount.error = "Please enter an amount"
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }
}