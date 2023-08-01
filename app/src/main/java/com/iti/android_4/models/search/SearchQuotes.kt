package com.iti.android_4.models.search

import com.iti.android_4.models.quotes.Result

data class SearchQuotes(
    val __info__: Info,
    val count: Int,
    val page: Int,
    val results: List<Result>,
    val totalCount: Int,
    val totalPages: Int
)