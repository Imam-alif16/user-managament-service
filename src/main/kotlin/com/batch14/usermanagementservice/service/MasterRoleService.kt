package com.batch14.usermanagementservice.service

import com.batch14.usermanagementservice.domain.dto.response.ResGetAllRoleDto

interface MasterRoleService {
    fun getAllRole(): List<ResGetAllRoleDto>
}