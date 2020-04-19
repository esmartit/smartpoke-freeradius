package com.esmartit.freeradiusservice.endpoints

import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Output
import org.springframework.http.ResponseEntity
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

private val LOGGER = LoggerFactory.getLogger(AccountingEndpoint::class.java)

@RestController
@EnableBinding(SessionActivityProducer::class)
class AccountingEndpoint(private val sessionActivityProducer: SessionActivityProducer) {

    @PostMapping("/user/{userName}/sessions", "/user/{userName}/sessions/{acctUniqueSessionID}")
    fun handle(
        @RequestBody body: FreeRadiusEvent,
        @PathVariable userName: String,
        @PathVariable(required = false) acctUniqueSessionID: String?
    ): ResponseEntity<Any> {

        CompletableFuture.supplyAsync {
            sessionActivityProducer.output().send(
                MessageBuilder
                    .withPayload(body)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, body.callingStationId.toByteArray())
                    .build()
            )
        }.whenComplete { r, ex ->
            r?.run { LOGGER.info("Message with key: ${body.callingStationId} sent") }
            ex?.run { LOGGER.error(message, this) }
        }

        return ResponseEntity.noContent().build()
    }
}

interface SessionActivityProducer {
    @Output("session-activity-producer")
    fun output(): MessageChannel
}