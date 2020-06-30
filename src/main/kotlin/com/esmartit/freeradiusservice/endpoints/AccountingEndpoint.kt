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
        @RequestBody body: AccountingActivity,
        @PathVariable userName: String,
        @PathVariable(required = false) acctUniqueSessionID: String?
    ): ResponseEntity<Any> {

        val event = body.toEvent()

        CompletableFuture.supplyAsync {
            sessionActivityProducer.output().send(
                MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, event.username.toByteArray())
                    .build()
            )
        }.whenComplete { r, ex ->
            r?.run { LOGGER.info("Message with key: ${body.callingStationId} sent") }
            ex?.run { LOGGER.error(message, this) }
        }

        return ResponseEntity.noContent().build()
    }
}

private fun AccountingActivity.toEvent(): FreeRadiusEvent {
    return FreeRadiusEvent(
        username = username,
        acctSessionId = acctSessionId,
        acctUniqueSessionId = acctUniqueSessionId,
        calledStationId = calledStationId,
        callingStationId = callingStationId,
        connectInfo = connectInfo,
        eventTimeStamp = eventTimeStamp,
        serviceType = serviceType,
        statusType = statusType
    )
}

data class AccountingActivity(
    val acctSessionId: String,
    val statusType: String,
    val acctUniqueSessionId: String,
    val calledStationId: String,
    val callingStationId: String,
    val connectInfo: String,
    val eventTimeStamp: String,
    val serviceType: String,
    val username: String
)

interface SessionActivityProducer {
    @Output("session-activity-producer")
    fun output(): MessageChannel
}