package com.batch14.usermanagementservice.repository

import com.batch14.usermanagementservice.domain.entity.MasterUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface MasterUserRepository: JpaRepository<MasterUserEntity, Int> {
    @Query("""
        SELECT U FROM MasterUserEntity U
        WHERE U.isDelete = false
        AND U.isActive = true
    """, nativeQuery = false)
    fun getAllActiveUser(): List<MasterUserEntity>

    @Query("""
        SELECT U FROM MasterUserEntity U
        WHERE U.isDelete = false
        AND U.isActive = true
        AND U.id = :id
    """)
    fun getActiveUserById(@Param("id") id: Int): MasterUserEntity?
    fun findFirstByEmail(email: String): MasterUserEntity?
    fun findFirstByUsername(username: String): Optional<MasterUserEntity>
}