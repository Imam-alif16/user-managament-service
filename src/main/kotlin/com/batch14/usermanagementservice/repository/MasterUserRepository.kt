package com.batch14.usermanagementservice.repository

import com.batch14.usermanagementservice.domain.entity.MasterUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface MasterUserRepository: JpaRepository<MasterUserEntity, Int> {
    @Query("""
        SELECT U FROM MasterUserEntity U
        WHERE U.isDeleted = false
        AND U.isActive = true
    """, nativeQuery = false)
    fun getAllActiveUser(): List<MasterUserEntity>

//    @Query("""
//        SELECT U FROM MasterUserEntity U
//        WHERE U.isDelete = false
//        AND U.isActive = true
//        AND U.id = :id
//    """)
//    fun getActiveUserById(@Param("id") id: Int): MasterUserEntity?
    fun findFirstByEmail(email: String): MasterUserEntity?
    @Query("""
        SELECT U FROM MasterUserEntity U
        WHERE U.isDeleted = false
        AND U.isActive = true
        AND U.username = :username
    """, nativeQuery = false)
    fun findFirstByUsername(username: String): Optional<MasterUserEntity>
    @Query("""
        SELECT u FROM MasterUserEntity u
        WHERE u.id IN (:ids)
    """, nativeQuery = false)
    fun findAllByIds(ids: List<Int>): List<MasterUserEntity>
    @Query("""
        SELECT U FROM MasterUserEntity U
        WHERE U.isDeleted = false
    """, nativeQuery = false)
    fun findByIsDeletedFalse(): List<MasterUserEntity>
    @Query("""
        SELECT U FROM MasterUserEntity U
        WHERE U.isDeleted = false
        AND U.id = :id
    """, nativeQuery = false)
    fun findByIdAndIsDeletedFalse(id: Int): Optional<MasterUserEntity>
}