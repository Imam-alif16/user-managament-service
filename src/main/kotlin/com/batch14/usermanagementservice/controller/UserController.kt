package com.batch14.usermanagementservice.controller

import com.batch14.usermanagementservice.domain.dto.request.ReqRegisterDto
import com.batch14.usermanagementservice.domain.dto.response.BaseResponse
import com.batch14.usermanagementservice.domain.dto.response.ResGetUserDto
import com.batch14.usermanagementservice.service.MasterUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val masterUserService: MasterUserService
) {
    @GetMapping("/active")
    fun getAllActiveUser(): ResponseEntity<
            BaseResponse<List<ResGetUserDto>>> {
        return ResponseEntity.ok(
            BaseResponse(
                data = masterUserService.findAllActiveUsers()
            )
        )
    }
    @GetMapping("/active/{id}")
    fun getActiveUserById(@PathVariable id: Int): ResponseEntity<ResGetUserDto>{
        return ResponseEntity.ok(
            masterUserService.findActiveUserById(id)
        )
    }

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody req: ReqRegisterDto
    ): ResponseEntity<BaseResponse<ResGetUserDto>> {
        return ResponseEntity(
            BaseResponse(
                data = masterUserService.register(req),
                message = "Register sukses"
            ),
            HttpStatus.CREATED
        )
    }
}