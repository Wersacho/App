package com.example.app.utils.firebase

data class FilterData(
    val minPrice: Int = 0,
    val maxPrice: Int = 0,
    val filterType: String = FirebaseConst.TITLE
)
