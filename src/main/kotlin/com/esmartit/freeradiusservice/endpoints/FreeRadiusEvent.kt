package com.esmartit.freeradiusservice.endpoints

data class FreeRadiusEvent(
    val acctSessionId: String,
    val statusType: String,
    val acctUniqueSessionId: String,
    val calledStationId: String,
    val callingStationId: String,
    val connectInfo: String,
    val eventTimeStamp: String,
    val serviceType: String,
    val username: String,
    val acctTerminateCause: String,
    val nasIpAddress: String,
    val acctSessionTime: String,
    val acctInputOctets: String,
    val acctOutputOctets: String
)