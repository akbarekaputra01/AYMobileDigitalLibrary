package com.example.aymobiledigitallibrary.data

import com.example.aymobiledigitallibrary.model.LocalRegion

data class LibraryItem(
    val id: String,
    val title: String,
    val authors: String,
    val year: Int,
    val category: String,
    val documentType: String,
    val citationCount: Int,
    val abstractPreview: String,
    val tags: List<String>,
    val paginationPage: Int,
    val scrollZoneIndex: Int,
    val localRegion: LocalRegion
)
