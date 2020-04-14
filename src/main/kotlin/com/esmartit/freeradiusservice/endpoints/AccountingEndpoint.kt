package com.esmartit.freeradiusservice.endpoints

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountingEndpoint {

    @PostMapping("/user/{userName}/sessions", "/user/{userName}/sessions/{acctUniqueSessionID}")
    fun handle(
        @RequestBody body: FreeRadiusEvent,
        @PathVariable userName: String,
        @PathVariable(required = false) acctUniqueSessionID: String?
    ): ResponseEntity<String> {
        return ResponseEntity.ok("ok")
    }
}