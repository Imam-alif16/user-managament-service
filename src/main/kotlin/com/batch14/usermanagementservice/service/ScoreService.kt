package com.batch14.usermanagementservice.service

interface ScoreService {
    fun testIncrementTransaction(testcase: String): String
    fun testSelfInvocationTransaction(testcase: String): String
    fun transferScore(from: String, to: String, score: Int): String
}