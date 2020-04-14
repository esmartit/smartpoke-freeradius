package com.esmartit.freeradiusservice.endpoints

import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Output
import org.springframework.http.ResponseEntity
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
@EnableBinding(AuthenticatedUsersProducer::class)
class PostAuthEndpoint(private val authenticatedUsersProducer: AuthenticatedUsersProducer) {

    @PostMapping("/user/{userName}/mac", "/user/{userName}/mac/{calledStationID}")
    fun handle(
        @RequestBody body: FreeRadiusEvent,
        @PathVariable userName: String,
        @PathVariable(required = false) calledStationID: String?,
        @RequestParam("action") action: String
    ): ResponseEntity<String> {
        CompletableFuture.runAsync {
            authenticatedUsersProducer.output().send(MessageBuilder.withPayload(body).build())
        }
        return ResponseEntity.ok("ok")
    }
}

interface AuthenticatedUsersProducer {
    @Output("authenticated-users-producer")
    fun output(): MessageChannel
}