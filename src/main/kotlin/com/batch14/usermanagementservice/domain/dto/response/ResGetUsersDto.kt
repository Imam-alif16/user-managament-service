package com.batch14.usermanagementservice.domain.dto.response

import java.io.Serial
import java.io.Serializable

class ResGetUsersDto (
    val id: Int,
    val email: String,
    val username: String,
    var roleId: Int? = null,
//    var roleName: String? = null
    var isDelete: Boolean? = null
): Serializable {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 8783090186345917598L
    }
}