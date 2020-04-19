package com.esmartit.freeradiusservice.endpoints

import com.esmartit.freeradiusservice.service.SignUpService
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Output
import org.springframework.http.ResponseEntity
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

private val LOGGER = LoggerFactory.getLogger(SignUpEndpoint::class.java)

@EnableBinding(RegisteredUsersProducer::class)
@RestController
class SignUpEndpoint(
    private val signUpService: SignUpService,
    private val registeredUsersProducer: RegisteredUsersProducer
) {

    @PostMapping("/user/signUp")
    fun handle(@RequestBody body: SignUpBody): ResponseEntity<String> {

        signUpService.execute(body)

        CompletableFuture.supplyAsync {
            registeredUsersProducer.output().send(
                MessageBuilder
                    .withPayload(body)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, body.username.toByteArray())
                    .build()
            )
        }.whenComplete { r, ex ->
            r?.run { LOGGER.info("Message with key: ${body.username} sent") }
            ex?.run { LOGGER.error(message, this) }
        }

        return ResponseEntity.ok("ok")
    }
}

data class SignUpBody(val username: String, val password: String, val groupName: String)

interface RegisteredUsersProducer {
    @Output("registered-users-producer")
    fun output(): MessageChannel
}