package com.esmartit.freeradiusservice.service

import com.esmartit.freeradiusservice.endpoints.PatchGroup
import com.esmartit.freeradiusservice.endpoints.SignUpBody
import com.esmartit.freeradiusservice.entities.RadCheckEntity
import com.esmartit.freeradiusservice.entities.RadCheckRepository
import com.esmartit.freeradiusservice.entities.RadUserGroupEntity
import com.esmartit.freeradiusservice.entities.RadUserGroupRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val radCheckRepository: RadCheckRepository,
    private val radUserGroupRepository: RadUserGroupRepository
) {

    fun save(body: SignUpBody) {

        val user = with(body) {
            radCheckRepository.findByUsername(username)?.run { throw IllegalArgumentException("user already exists") }
                ?: RadCheckEntity(0, username, "Cleartext-Password", ":=", password)
        }

        val group = with(body) {
            radUserGroupRepository.findByUsername(username)?.copy(username = username, groupName = groupName)
                ?: RadUserGroupEntity(0, username, groupName, 1)
        }

        radCheckRepository.save(user)
        radUserGroupRepository.save(group)
    }

    fun update(body: SignUpBody) {

        val user = with(body) {
            radCheckRepository.findByUsername(username)?.copy(username = username, value = password)
                ?: throw IllegalArgumentException("user does not exist")
        }

        val group = with(body) {
            radUserGroupRepository.findByUsername(username)?.copy(username = username, groupName = groupName)
                ?: RadUserGroupEntity(0, username, groupName, 1)
        }

        radCheckRepository.save(user)
        radUserGroupRepository.save(group)
    }

    fun patchGroup(body: PatchGroup) {

        val group = with(body) {
            radUserGroupRepository.findByUsername(username)?.copy(username = username, groupName = groupName)
                ?: throw IllegalArgumentException("user is not registered: $body")
        }

        radUserGroupRepository.save(group)
    }
}