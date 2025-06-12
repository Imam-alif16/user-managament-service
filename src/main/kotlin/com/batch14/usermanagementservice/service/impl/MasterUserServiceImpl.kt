package com.batch14.usermanagementservice.service.impl

import com.batch14.usermanagementservice.domain.dto.request.ReqLoginDto
import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterDto
import com.batch14.usermanagementservice.domain.dto.response.ResGetUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResLoginDto
import com.batch14.usermanagementservice.domain.entity.MasterUserEntity
import com.batch14.usermanagementservice.exception.CustomException
import com.batch14.usermanagementservice.repository.MasterRoleRepository
import com.batch14.usermanagementservice.repository.MasterUserRepository
import com.batch14.usermanagementservice.service.MasterUserService
import com.batch14.usermanagementservice.util.BCryptUtil
import com.batch14.usermanagementservice.util.JwtUtil
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class MasterUserServiceImpl(
    private val masterUserRepository: MasterUserRepository,
    private val masterRoleRepository: MasterRoleRepository,
    private val jwtUtil: JwtUtil,
    private val bcrypt: BCryptUtil
): MasterUserService {
    override fun findAllActiveUsers(): List<ResGetUserDto> {
        val rawData = masterUserRepository.getAllActiveUser()
        val result = mutableListOf<ResGetUserDto>()
        //TUGAS BUAT get user by id kalau ketemu tampilin kalo engga, error 500 gapapa
        //GET ALL USERS
        rawData.forEach { u ->
            result.add(
                ResGetUserDto(
                    username = u.username,
                    id = u.id,
                    email = u.email,
                    //jika user memiliki role maka ambil id role
                    //jika user tidak memiliki role maka value null
                    //GET ROLE by USER.role.id
                    roleId = u.role?.id,
                    //jika user memiliki role maka ambil name role
                    //jika user tidak memiliki role maka value null
//                    roleName = u.role?.name
                )
            )
        }
        return result
    }

    override fun findActiveUserById(id: Int): ResGetUserDto {
        val rawData = masterUserRepository.getActiveUserById(id)!!
        val result = ResGetUserDto(
            username = rawData.username,
            id = rawData.id,
            email = rawData.email,
            roleId = rawData.role?.id,
//            roleName = rawData.role?.name
        )
        return result
    }

    override fun register(req: ReqRegisterDto): ResGetUserDto {
        val role = if(req.roleId == null){
            Optional.empty() //optional kosong, berbeda dengan null
        }else{
            masterRoleRepository.findById(req.roleId)
        }

        if(role.isEmpty && req.roleId != null){
            throw CustomException("Role ${req.roleId} tidak ditemukan", 400)
        }

        //cek apakah email sudah terdafttar
        val existingUserEmail = masterUserRepository.findFirstByEmail(req.email)
        if(existingUserEmail != null) {
            throw CustomException("Email sudah terdaftar", 400)
        }

        val existingUsername = masterUserRepository
            .findFirstByUsername(req.username)

        if(existingUsername.isPresent) {
            throw CustomException("Username sudah terdaftar", 400)
        }

        val hashPw = bcrypt.hash(req.password)

        val userRaw = MasterUserEntity(
            email = req.email,
            password = hashPw,
            username = req.username,
            role = if(role.isPresent){
                role.get()
            }else{
                null
            }
        )
        //entity/row dari hasil save di jadikan sebagai return value
        val user = masterUserRepository.save(userRaw)
        return ResGetUserDto(
            id = user.id,
            email = user.email,
            username = user.username,
            roleId = user.role?.id
        )
    }

    override fun login(req: ReqLoginDto): ResLoginDto {
        val userEntityOpt = masterUserRepository.findFirstByUsername(req.username)

        if (userEntityOpt.isEmpty) {
            throw CustomException("Username atau Password salah", 400)
        }

        val userEntity = userEntityOpt.get()

        if(!bcrypt.verify(req.password, userEntity.password)) {
            throw CustomException("Username atau Password Salah", 400)
        }

        val role = if (userEntity.role != null) {
            userEntity.role!!.name
        }else {
            "user"
        }

        val token = jwtUtil.generateToken(userEntity.id, role)

        return ResLoginDto(token)

    }

}