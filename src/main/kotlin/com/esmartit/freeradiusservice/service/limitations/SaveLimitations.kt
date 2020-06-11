package com.esmartit.freeradiusservice.service.limitations

import com.esmartit.freeradiusservice.entities.LimitationEntity
import com.esmartit.freeradiusservice.entities.RadGroupCheckEntity
import com.esmartit.freeradiusservice.entities.RadGroupCheckRepository
import com.esmartit.freeradiusservice.entities.RadGroupReplyEntity
import com.esmartit.freeradiusservice.entities.RadGroupReplyRepository

class SaveLimitations(
    private val radGroupReplyRepository: RadGroupReplyRepository,
    private val radGroupCheckRepository: RadGroupCheckRepository
) {

    fun execute(limitation: LimitationEntity): LimitationEntity {
        saveOrDeleteAcctInterimInterval(limitation)
        saveOrDeleteMaxUpload(limitation)
        saveOrDeleteMaxDownload(limitation)
        saveOrDeleteRedirectUrl(limitation)
        saveOrDeleteMaxTraffic(limitation)
        saveOrDeleteAccessPeriod(limitation)
        saveOrDeleteDailySession(limitation)

        return limitation
    }

    private fun saveOrDeleteDailySession(entity: LimitationEntity) {
        val dailySession = DAILY_SESSION
        val groupName = entity.name
        entity.dailySession?.run { actualValueString() }
            ?.run { getRadGroupCheckEntity(groupName, dailySession, this) }
            ?.run { radGroupCheckRepository.save(this) }
            ?: radGroupCheckRepository.deleteByGroupNameAndAttribute(groupName, dailySession)
    }

    private fun saveOrDeleteAccessPeriod(entity: LimitationEntity) {
        val groupName = entity.name
        val accessPeriod = ACCESS_PERIOD
        entity.accessPeriod?.run { actualValueString() }
            ?.run { getRadGroupCheckEntity(groupName, accessPeriod, this) }
            ?.run { radGroupCheckRepository.save(this) }
            ?: radGroupCheckRepository.deleteByGroupNameAndAttribute(groupName, accessPeriod)
    }

    private fun saveOrDeleteMaxTraffic(entity: LimitationEntity) {
        val groupName = entity.name
        val maxTraffic = MAX_TRAFFIC
        entity.maxTraffic?.run { actualValueString() }
            ?.run { getRadGroupCheckEntity(groupName, maxTraffic, this) }
            ?.run { radGroupCheckRepository.save(this) }
            ?: radGroupCheckRepository.deleteByGroupNameAndAttribute(groupName, maxTraffic)
    }

    private fun saveOrDeleteRedirectUrl(entity: LimitationEntity) {
        val groupName = entity.name
        val redirectUrl = REDIRECT_URL
        entity.urlRedirect?.run { getRadGroupReplyEntity(groupName, redirectUrl, this) }
            ?.run { radGroupReplyRepository.save(this) }
            ?: radGroupReplyRepository.deleteByGroupNameAndAttribute(groupName, redirectUrl)
    }

    private fun saveOrDeleteMaxDownload(entity: LimitationEntity) {
        val groupName = entity.name
        val maxDownload = MAX_DOWNLOAD
        entity.maxDownload?.run { actualValueString() }
            ?.run { getRadGroupReplyEntity(groupName, maxDownload, this) }
            ?.run { radGroupReplyRepository.save(this) }
            ?: radGroupReplyRepository.deleteByGroupNameAndAttribute(groupName, maxDownload)
    }

    private fun saveOrDeleteMaxUpload(entity: LimitationEntity) {
        val groupName = entity.name
        val maxUpload = MAX_UPLOAD
        entity.maxUpload?.run { actualValueString() }
            ?.run { getRadGroupReplyEntity(groupName, maxUpload, this) }
            ?.run { radGroupReplyRepository.save(this) }
            ?: radGroupReplyRepository.deleteByGroupNameAndAttribute(groupName, maxUpload)
    }

    private fun saveOrDeleteAcctInterimInterval(entity: LimitationEntity) {
        getRadGroupReplyEntity(entity.name,
            ACCT_INTERIM_INTERVAL, "60").run { radGroupReplyRepository.save(this) }
    }

    private fun getRadGroupReplyEntity(groupName: String, attribute: String, value: String): RadGroupReplyEntity {
        return (radGroupReplyRepository.findByGroupNameAndAttribute(groupName, attribute)?.copy(value = value)
            ?: RadGroupReplyEntity(0, groupName, attribute,
                EQUAL_OP, value))
    }

    private fun getRadGroupCheckEntity(groupName: String, attribute: String, value: String): RadGroupCheckEntity {
        return (radGroupCheckRepository.findByGroupNameAndAttribute(groupName, attribute)?.copy(value = value)
            ?: RadGroupCheckEntity(0, groupName, attribute,
                EQUAL_OP, value))
    }
}