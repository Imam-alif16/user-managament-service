package com.batch14.usermanagementservice.repository

import com.batch14.usermanagementservice.domain.entity.ScoreEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ScoreRepository: JpaRepository<ScoreEntity, Long> {
    fun findFirstByName(name: String): Optional<ScoreEntity>
}