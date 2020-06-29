package com.esmartit.freeradiusservice.endpoints

import com.esmartit.freeradiusservice.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Output
import org.springframework.http.HttpStatus
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.concurrent.CompletableFuture

private val LOGGER = LoggerFactory.getLogger(SplashEndpoint::class.java)

@RestController
@RequestMapping("/api/users")
@EnableBinding(RegisteredUsersProducer::class)
class SplashEndpoint(
    private val userService: UserService,
    private val registeredUsersProducer: RegisteredUsersProducer
) {

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun signUp(@RequestBody body: SignUpBody) {

        userService.save(body)

        CompletableFuture.supplyAsync { sendEvent(body) }
            .whenComplete { r, ex ->
                r?.run { LOGGER.info("Message with key: ${body.username} sent") }
                ex?.run { LOGGER.error(message, this) }
            }
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@RequestBody body: SignUpBody) {
        userService.update(body)
    }

    @PatchMapping("/group")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun patchGroup(@RequestBody body: PatchGroup) {
        userService.patchGroup(body)
    }

    private fun sendEvent(body: SignUpBody): Boolean {
        return registeredUsersProducer.output().send(
            MessageBuilder
                .withPayload(
                    SignUpEvent(
                        username = body.username,
                        clientMac = body.clientMac,
                        dateOfBirth = body.bDate,
                        gender = body.gender,
                        zipCode = body.zipCode,
                        memberShip = body.memberShip,
                        spotId = body.spotId,
                        hotspotName = body.hotspotName,
                        seenTimeEpoch = Instant.now().toEpochMilli()
                    )
                )
                .setHeader(KafkaHeaders.MESSAGE_KEY, body.username.toByteArray())
                .build()
        )
    }
}

data class SignUpBody(
    val username: String,
    val password: String,
    val groupName: String,
    val clientMac: String,
    val bDate: String,
    val gender: String,
    val zipCode: String,
    val memberShip: String,
    val spotId: String,
    val hotspotName: String
)

data class SignUpEvent(
    val username: String,
    val clientMac: String,
    val dateOfBirth: String,
    val gender: String,
    val zipCode: String,
    val memberShip: String,
    val spotId: String,
    val hotspotName: String,
    val seenTimeEpoch: Long
)

data class PatchGroup(val username: String, val groupName: String)

interface RegisteredUsersProducer {
    @Output("registered-users-producer")
    fun output(): MessageChannel
}