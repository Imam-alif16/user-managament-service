package com.batch14.usermanagementservice.domain.dto.request

data class ReqTransferDto(
    val from: String,
    val to: String,
    val score: Int
)