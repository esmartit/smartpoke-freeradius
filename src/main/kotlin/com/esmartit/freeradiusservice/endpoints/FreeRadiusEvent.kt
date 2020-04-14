package com.esmartit.freeradiusservice.endpoints

import java.time.Instant

data class FreeRadiusEvent(
    val acctSessionId: String,
    val acctUniqueSessionId: String,
    val calledStationId: String,
    val callingStationId: String,
    val connectInfo: String,
    val eventTimeStamp: Instant,
    val serviceType: String,
    val username: String
)