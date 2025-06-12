package com.batch14.usermanagementservice.service.impl

import com.batch14.usermanagementservice.domain.dto.response.ResGetAllRoleDto
import com.batch14.usermanagementservice.repository.MasterRoleRepository
import com.batch14.usermanagementservice.service.MasterRoleService
import org.springframework.stereotype.Service

@Service
class MasterRoleServiceImpl(
    private val masterRoleRepository: MasterRoleRepository
): MasterRoleService {
    override fun getAllRole(): List<ResGetAllRoleDto> {
        val rawRole = masterRoleRepository.findAll()
        val result = mutableListOf<ResGetAllRoleDto>()
        rawRole.forEach {role ->
            result.add(
                ResGetAllRoleDto(
                    id = role.id,
                    name = role.name
                )
            )
        }
        return result
    }
}