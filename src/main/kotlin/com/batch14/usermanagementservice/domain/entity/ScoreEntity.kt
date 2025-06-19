package com.batch14.usermanagementservice.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "mst_scores")
data class ScoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    var name: String? = null,
    var score: Int? = null,
    var hasTransferred: Boolean? = null,
)