package com.iti.android_4.models.quotes

import androidx.annotation.Keep

@Keep
data class Quotes(
    val count: Int,
    val lastItemIndex: Int,
    val page: Int,
    val results: List<Result>,
    val totalCount: Int,
    val totalPages: Int
)