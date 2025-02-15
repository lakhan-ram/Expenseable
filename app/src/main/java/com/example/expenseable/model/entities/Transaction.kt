package com.example.expenseable.model.entities

data class Transaction(
    val id: String,
    val title: String,
    val amount: String,
    val type: String,
    val date: String,
    val userId: String
)
