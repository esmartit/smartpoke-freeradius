package com.esmartit.freeradiusservice.entities

import org.springframework.hateoas.RepresentationModel

data class LimitationEntity(
    val name: String,
    val maxUpload: DataRate? = null,
    val maxDownload: DataRate? = null,
    val maxTraffic: DataTraffic? = null,
    val urlRedirect: String? = null,
    val accessPeriod: TimeRestriction? = null,
    val dailySession: TimeRestriction? = null
) : RepresentationModel<LimitationEntity>()


data class DataRate(val value: Long, val rate: Rate) {
    private fun actualValue(): Long {
        return value * rate.value
    }

    fun actualValueString(): String {
        return actualValue().toString()
    }
}

enum class Rate(val value: Long) {
    KBPS(1024), MBPS(1048576), GBPS(1073741824);

    companion object {
        fun fromValue(value: Long): DataRate {
            return when {
                value / GBPS.value > 0 -> DataRate(value / GBPS.value, GBPS)
                value / MBPS.value > 0 -> DataRate(value / MBPS.value, MBPS)
                else -> DataRate(value / KBPS.value, KBPS)
            }
        }
    }
}

data class DataTraffic(val value: Long, val traffic: Traffic) {
    private fun actualValue(): Long {
        return value * traffic.value
    }

    fun actualValueString(): String {
        return actualValue().toString()
    }
}

enum class Traffic(val value: Long) {
    KB(1024), MB(1048576), GB(1073741824);

    companion object {
        fun fromValue(value: Long): DataTraffic {
            return when {
                value / GB.value > 0 -> DataTraffic(value / GB.value, GB)
                value / MB.value > 0 -> DataTraffic(value / MB.value, MB)
                else -> DataTraffic(value / KB.value, KB)
            }
        }
    }
}

data class TimeRestriction(val value: Long, val period: Period) {
    private fun actualValue(): Long {
        return value * period.value
    }

    fun actualValueString(): String {
        return actualValue().toString()
    }
}

enum class Period(val value: Long) {
    MINUTES(60), HOURS(3600), DAYS(86400);

    companion object {
        fun fromValue(value: Long): TimeRestriction {
            return when {
                value / DAYS.value > 0 -> TimeRestriction(value / DAYS.value, DAYS)
                value / HOURS.value > 0 -> TimeRestriction(value / HOURS.value, HOURS)
                else -> TimeRestriction(value / MINUTES.value, MINUTES)
            }
        }
    }
}
