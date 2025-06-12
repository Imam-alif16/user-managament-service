package com.batch14.usermanagementservice.exception

class CustomException(
    val exceptionMessage: String,
    val statusCode: Int,
    val data: Any? = null
): RuntimeException()