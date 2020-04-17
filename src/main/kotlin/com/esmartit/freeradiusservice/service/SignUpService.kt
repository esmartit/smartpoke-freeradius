package com.esmartit.freeradiusservice.service

import com.esmartit.freeradiusservice.endpoints.SignUpBody
import com.esmartit.freeradiusservice.entities.RadCheckEntity
import com.esmartit.freeradiusservice.entities.RadCheckRepository
import com.esmartit.freeradiusservice.entities.RadUserGroupEntity
import com.esmartit.freeradiusservice.entities.RadUserGroupRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SignUpService(
    private val radCheckRepository: RadCheckRepository,
    private val radUserGroupRepository: RadUserGroupRepository
) {

    fun execute(body: SignUpBody) {
        radCheckRepository.save(RadCheckEntity(0, body.username, "Cleartext-Password", ":=", body.password))
        radUserGroupRepository.save(RadUserGroupEntity(0, body.username, body.groupName, 1))
    }
}