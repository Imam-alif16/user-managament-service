package com.batch14.usermanagementservice.repository

import com.batch14.usermanagementservice.domain.entity.MasterRoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MasterRoleRepository: JpaRepository<MasterRoleEntity, Int> {
}