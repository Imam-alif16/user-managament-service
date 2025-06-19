package com.batch14.usermanagementservice.service

import com.batch14.usermanagementservice.domain.entity.ScoreEntity

interface TransferService {
    fun processTransfer(from: ScoreEntity, to: ScoreEntity, score: Int): String
}