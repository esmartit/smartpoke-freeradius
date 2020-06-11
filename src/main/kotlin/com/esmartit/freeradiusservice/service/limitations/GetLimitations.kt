package com.esmartit.freeradiusservice.service.limitations

import com.esmartit.freeradiusservice.entities.LimitationEntity
import com.esmartit.freeradiusservice.entities.Period
import com.esmartit.freeradiusservice.entities.RadGroupCheckEntity
import com.esmartit.freeradiusservice.entities.RadGroupCheckRepository
import com.esmartit.freeradiusservice.entities.RadGroupReplyEntity
import com.esmartit.freeradiusservice.entities.RadGroupReplyRepository
import com.esmartit.freeradiusservice.entities.Rate
import com.esmartit.freeradiusservice.entities.Traffic

class GetLimitations(
    private val radGroupReplyRepository: RadGroupReplyRepository,
    private val radGroupCheckRepository: RadGroupCheckRepository
) {

    fun getAll(): List<LimitationEntity> {
        return (radGroupCheckRepository.findAll().map { groupToGeneric(it) } + radGroupReplyRepository.findAll()
            .map { replyToGeneric(it) })
            .groupingBy { it.groupName }
            .fold(mutableMapOf<String, String>(), { acc, entity -> reduceToProperties(entity, acc) })
            .map { createLimitation(it.value, it.key) }
    }

    fun getByName(name: String): LimitationEntity? {

        return (radGroupCheckRepository.findByGroupName(name)
            .map { groupToGeneric(it) } + radGroupReplyRepository.findByGroupName(name)
            .map { replyToGeneric(it) })
            .groupingBy { it.groupName }
            .fold(mutableMapOf<String, String>(), { acc, entity -> reduceToProperties(entity, acc) })
            .map { createLimitation(it.value, it.key) }
            .firstOrNull()
    }

    private fun reduceToProperties(
        entity: GenericRad,
        acc: MutableMap<String, String>
    ): MutableMap<String, String> {
        return when (entity.attribute) {
            ACCT_INTERIM_INTERVAL -> acc.apply { this[ACCT_INTERIM_INTERVAL] = entity.value }
            MAX_UPLOAD -> acc.apply { this[MAX_UPLOAD] = entity.value }
            MAX_DOWNLOAD -> acc.apply { this[MAX_DOWNLOAD] = entity.value }
            REDIRECT_URL -> acc.apply { this[REDIRECT_URL] = entity.value }
            MAX_TRAFFIC -> acc.apply { this[MAX_TRAFFIC] = entity.value }
            ACCESS_PERIOD -> acc.apply { this[ACCESS_PERIOD] = entity.value }
            DAILY_SESSION -> acc.apply { this[DAILY_SESSION] = entity.value }
            else -> acc
        }
    }

    private fun replyToGeneric(it: RadGroupReplyEntity) =
        GenericRad(
            it.groupName,
            it.attribute,
            it.value
        )

    private fun groupToGeneric(it: RadGroupCheckEntity) =
        GenericRad(
            it.groupName,
            it.attribute,
            it.value
        )

    private fun createLimitation(
        propertiesMap: MutableMap<String, String>,
        name: String
    ): LimitationEntity {
        val maxUpload = propertiesMap[MAX_UPLOAD]?.toLong()?.let { Rate.fromValue(it) }
        val maxDownload = propertiesMap[MAX_DOWNLOAD]?.toLong()?.let { Rate.fromValue(it) }
        val maxTraffic = propertiesMap[MAX_TRAFFIC]?.toLong()?.let { Traffic.fromValue(it) }
        val urlRedirect = propertiesMap[REDIRECT_URL]
        val accessPeriod = propertiesMap[ACCESS_PERIOD]?.toLong()?.let { Period.fromValue(it) }
        val dailySession = propertiesMap[DAILY_SESSION]?.toLong()?.let { Period.fromValue(it) }
        return LimitationEntity(
            name,
            maxUpload,
            maxDownload,
            maxTraffic,
            urlRedirect,
            accessPeriod,
            dailySession
        )
    }
}

data class GenericRad(
    val groupName: String,
    val attribute: String,
    val value: String
)