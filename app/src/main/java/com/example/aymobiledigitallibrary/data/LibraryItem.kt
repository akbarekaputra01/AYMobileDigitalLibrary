package com.example.aymobiledigitallibrary.data

data class LibraryItem(
    val id: String,
    val title: String,
    val authors: String, // Ensure this exists
    val year: String,
    val category: String,
    val imagePath: String
)
