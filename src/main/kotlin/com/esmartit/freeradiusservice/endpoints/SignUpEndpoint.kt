package com.esmartit.freeradiusservice.endpoints

import com.esmartit.freeradiusservice.entities.RadCheckEntity
import com.esmartit.freeradiusservice.entities.RadCheckRepository
import com.esmartit.freeradiusservice.entities.RadUserGroupEntity
import com.esmartit.freeradiusservice.entities.RadUserGroupRepository
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Output
import org.springframework.http.ResponseEntity
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@EnableBinding(RegisteredUsersProducer::class)
@RestController
class SignUpEndpoint(
    private val radCheckRepository: RadCheckRepository,
    private val radUserGroupRepository: RadUserGroupRepository,
    private val registeredUsersProducer: RegisteredUsersProducer
) {

    @PostMapping("/user/signUp")
    fun handle(@RequestBody body: SignUpBody): ResponseEntity<String> {
        radCheckRepository.save(RadCheckEntity(0, body.username, "Cleartext-Password", ":=", body.password))
        radUserGroupRepository.save(RadUserGroupEntity(0, body.username, body.groupName, 1))
        CompletableFuture.runAsync { registeredUsersProducer.output().send(MessageBuilder.withPayload(body).build()) }
        return ResponseEntity.ok("ok")
    }
}

data class SignUpBody(val username: String, val password: String, val groupName: String)

interface RegisteredUsersProducer {
    @Output("registered-users-producer")
    fun output(): MessageChannel
}