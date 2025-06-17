package com.batch14.usermanagementservice.service

import com.batch14.usermanagementservice.domain.dto.request.ReqLoginDto
import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterDto
import com.batch14.usermanagementservice.domain.dto.request.ReqUpdateUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResGetUsersDto
import com.batch14.usermanagementservice.domain.dto.response.ResLoginDto

interface MasterUserService {
    fun findAllActiveUsers(): List<ResGetUsersDto>
//    fun findActiveUserById(id: Int): ResGetUsersDto
    fun register(req: ReqRegisterDto): ResGetUsersDto
    fun login(req: ReqLoginDto): ResLoginDto
    fun findUserById(id: Int): ResGetUsersDto
    fun findUsersByIds(ids: List<Int>): List<ResGetUsersDto>
    fun updateUser(req: ReqUpdateUserDto, userId: Int): ResGetUsersDto
    fun softDeleteUser(id: Int): ResGetUsersDto
    fun hardDeleteUser(id: Int): ResGetUsersDto
}