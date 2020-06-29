package com.esmartit.freeradiusservice.service.limitations

import com.esmartit.freeradiusservice.entities.LimitationEntity
import com.esmartit.freeradiusservice.entities.RadGroupCheckRepository
import com.esmartit.freeradiusservice.entities.RadGroupReplyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

const val ACCT_INTERIM_INTERVAL = "Acct-Interim-Interval"
const val MAX_UPLOAD = "Maximum-Data-Rate-Upstream"
const val MAX_DOWNLOAD = "Maximum-Data-Rate-Downstream"
const val REDIRECT_URL = "WISPr-Redirection-URL"
const val MAX_TRAFFIC = "Max-Daily-Octets"
const val ACCESS_PERIOD = "Access-Period"
const val DAILY_SESSION = "Max-Daily-Session"

const val EQUAL_OP = ":="

@Service
@Transactional
class LimitationService(
    private val radGroupCheckRepository: RadGroupCheckRepository,
    private val radGroupReplyRepository: RadGroupReplyRepository
) {

    fun getAll(): List<LimitationEntity> {
        return GetLimitations(radGroupReplyRepository, radGroupCheckRepository).getAll()
    }

    fun getByName(name: String): LimitationEntity? {

        return GetLimitations(radGroupReplyRepository, radGroupCheckRepository).getByName(name)
    }

    fun save(limitation: LimitationEntity): LimitationEntity {
        return SaveLimitations(radGroupReplyRepository, radGroupCheckRepository).execute(limitation)
    }

    fun delete(name: String) {
        radGroupReplyRepository.deleteByGroupName(name)
        radGroupCheckRepository.deleteByGroupName(name)
    }
}
