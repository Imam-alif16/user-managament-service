package com.batch14.usermanagementservice.controller

import com.batch14.usermanagementservice.domain.constant.Constant
import com.batch14.usermanagementservice.domain.dto.request.ReqLoginDto
import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterDto
import com.batch14.usermanagementservice.domain.dto.request.ReqTransferDto
import com.batch14.usermanagementservice.domain.dto.request.ReqUpdateUserDto
import com.batch14.usermanagementservice.domain.dto.response.BaseResponse
import com.batch14.usermanagementservice.domain.dto.response.ResGetUsersDto
import com.batch14.usermanagementservice.domain.dto.response.ResLoginDto
import com.batch14.usermanagementservice.service.MasterUserService
import com.batch14.usermanagementservice.service.ScoreService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val masterUserService: MasterUserService,
    private val httpServletRequest: HttpServletRequest,
    private val scoreService: ScoreService,
) {
    @GetMapping("/active")
    fun getAllActiveUser(): ResponseEntity<
            BaseResponse<List<ResGetUsersDto>>> {
        return ResponseEntity.ok(
            BaseResponse(
                data = masterUserService.findAllActiveUsers()
            )
        )
    }
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Int): ResponseEntity<BaseResponse<ResGetUsersDto>> {
        return ResponseEntity.ok(
            BaseResponse(
                data = masterUserService.findUserById(id)
            )
        )
    }

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody req: ReqRegisterDto
    ): ResponseEntity<BaseResponse<ResGetUsersDto>> {
        return ResponseEntity(
            BaseResponse(
                data = masterUserService.register(req),
                message = "Register sukses"
            ),
            HttpStatus.CREATED
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody req: ReqLoginDto
    ): ResponseEntity<BaseResponse<ResLoginDto>> {
        return ResponseEntity(
            BaseResponse(
                data = masterUserService.login(req),
                message = "Login sukses"
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("/test")
    fun testTransaction(
        @RequestParam testcase: String
    ): ResponseEntity<BaseResponse<String>> {
        val response = ResponseEntity.ok(BaseResponse(
            data = scoreService.testIncrementTransaction(testcase)
        ))
        return response
    }

    @PostMapping("/test")
    fun transferScore(
        @RequestBody req: ReqTransferDto
    ): ResponseEntity<BaseResponse<String>> {
        val response = ResponseEntity(
            BaseResponse(
                data = scoreService.transferScore(req.from, req.to, req.score),
                message = "Success Transfer Score"
            ),
            HttpStatus.OK
        )
        return response
    }

    @PutMapping
    fun updateUser(
        @RequestBody req: ReqUpdateUserDto
    ): ResponseEntity<BaseResponse<ResGetUsersDto>>{
        val userId = httpServletRequest.getHeader(Constant.HEADER_USER_ID)
        return ResponseEntity.ok(
            BaseResponse(
            data = masterUserService.updateUser(req, userId.toInt())
            )
        )
    }

    @PutMapping("/{id}/soft-delete")
    fun softDeleteUser(
        @PathVariable id: Int
    ): ResponseEntity<BaseResponse<ResGetUsersDto>>{
        return ResponseEntity.ok(
            BaseResponse(
                data = masterUserService.softDeleteUser(id),
                message = "Berhasil soft delete user"
            )
        )
    }

    @DeleteMapping("/{id}/hard-delete")
    fun hardDeleteUser(
        @PathVariable id: Int
    ): ResponseEntity<BaseResponse<ResGetUsersDto>>{
        return ResponseEntity.ok(
            BaseResponse(
                data = masterUserService.hardDeleteUser(id),
                message = "Berhasil hard delete user"
            )
        )
    }

}