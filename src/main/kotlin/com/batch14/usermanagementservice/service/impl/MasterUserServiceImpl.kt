package com.batch14.usermanagementservice.service.impl


import com.batch14.usermanagementservice.domain.dto.request.ReqLoginDto
import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterDto
import com.batch14.usermanagementservice.domain.dto.request.ReqUpdateUserDto
import com.batch14.usermanagementservice.domain.dto.response.ResGetUsersDto
import com.batch14.usermanagementservice.domain.dto.response.ResLoginDto
import com.batch14.usermanagementservice.domain.entity.MasterUserEntity
import com.batch14.usermanagementservice.exception.CustomException
import com.batch14.usermanagementservice.repository.MasterRoleRepository
import com.batch14.usermanagementservice.repository.MasterUserRepository
import com.batch14.usermanagementservice.service.MasterUserService
import com.batch14.usermanagementservice.util.BCryptUtil
import com.batch14.usermanagementservice.util.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class MasterUserServiceImpl(
    private val masterUserRepository: MasterUserRepository,
    private val masterRoleRepository: MasterRoleRepository,
    private val jwtUtil: JwtUtil,
    private val bcrypt: BCryptUtil,
    private val httpServletRequest: HttpServletRequest
): MasterUserService {
    override fun findAllActiveUsers(): List<ResGetUsersDto> {
        val rawData = masterUserRepository.getAllActiveUser()
        val result = mutableListOf<ResGetUsersDto>()
        //TUGAS BUAT get user by id kalau ketemu tampilin kalo engga, error 500 gapapa
        //GET ALL USERS
        rawData.forEach { u ->
            result.add(
                ResGetUsersDto(
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

//    override fun findActiveUserById(id: Int): ResGetUsersDto {
//        val rawData = masterUserRepository.getActiveUserById(id)!!
//        val result = ResGetUsersDto(
//            username = rawData.username,
//            id = rawData.id,
//            email = rawData.email,
//            roleId = rawData.role?.id,
////            roleName = rawData.role?.name
//        )
//        return result
//    }

    override fun register(req: ReqRegisterDto): ResGetUsersDto {
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
        return ResGetUsersDto(
            id = user.id,
            email = user.email,
            username = user.username,
            roleId = user.role?.id
        )
    }

    override fun login(req: ReqLoginDto): ResLoginDto {
        //hasilnya user dengan is_delete false dan is_active true
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

    //kalau data belum ada di redis bakal disimpan
    // kalau data di redis udah ada bakal langsung return data dari redis
    @Cacheable(
        "getUserById",
        key = "{#id}"
    )
    override fun findUserById(id: Int): ResGetUsersDto {
        val user = masterUserRepository.findById(id).orElseThrow {
            throw CustomException("User with id ${id} not found!!!", 400)
        }
        return ResGetUsersDto(
            id = user.id,
            email = user.email,
            username = user.username,
            roleId = user.role?.id,
        )
    }

    override fun findUsersByIds(ids: List<Int>): List<ResGetUsersDto> {
        val rawData = masterUserRepository.findAllByIds(
            ids
        )
        return rawData.map {
            ResGetUsersDto(
                id = it.id,
                username = it.username,
                email = it.email
            )
        }
    }

    @CacheEvict(
        value = ["getUserById"],
        key = "{#userId}"
    )
    override fun updateUser(
        req: ReqUpdateUserDto,
        userId: Int
    ): ResGetUsersDto {
        println("userId $userId")
        val user = masterUserRepository.findById(userId.toInt()).orElseThrow {
            throw CustomException(
                "User id $userId tidak ditemukan",
                HttpStatus.BAD_REQUEST.value())
        }

        val existingUser = masterUserRepository.findFirstByUsername(req.username)
        if(existingUser.isPresent){
            if(existingUser.get().id != user.id) {
                throw CustomException(
                    "Username sudah terdaftar",
                    HttpStatus.BAD_REQUEST.value())
            }
        }

        val existingUserEmail = masterUserRepository.findFirstByEmail(req.email)
        if(existingUserEmail != null) {
            if(existingUserEmail.id != user.id){
                throw CustomException(
                    "Email sudah terdaftar",
                    HttpStatus.BAD_REQUEST.value()
                )
            }
        }

        user.email  = req.email
        user.username = req.username
        user.updatedBy = userId.toString()

        val result = masterUserRepository.save(user)

        return ResGetUsersDto(
            id = result.id,
            username = result.username,
            email = result.email
        )
    }

    override fun softDeleteUser(id: Int): ResGetUsersDto {
        val user = masterUserRepository.findByIdAndIsDeletedFalse(id).orElseThrow {
            throw CustomException(
                "User dengan id ${id} tidak ditemukan atau sudah terhapus",
                HttpStatus.BAD_REQUEST.value()
            )
        }

        user.isDeleted = true
        masterUserRepository.save(user)

        return ResGetUsersDto(
            id = user.id,
            email = user.email,
            username = user.username,
            isDelete = user.isDeleted
        )
    }



    override fun hardDeleteUser(id: Int): ResGetUsersDto {
        val user = masterUserRepository.findById(id).orElseThrow {
            throw CustomException(
                "User dengan ide ${id} tidak ditemukan atau sudah terhapus",
                HttpStatus.BAD_REQUEST.value()
            )
        }

        masterUserRepository.delete(user)

        return ResGetUsersDto(
            id = user.id,
            email = user.email,
            username = user.username
        )
    }

}