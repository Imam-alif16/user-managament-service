package com.batch14.usermanagementservice.domain.dto.response

import jakarta.persistence.Column

class ResGetUserDto (
    val id: Int,
    val email: String,
    val username: String,
    var roleId: Int? = null
//    var roleName: String? = null
)