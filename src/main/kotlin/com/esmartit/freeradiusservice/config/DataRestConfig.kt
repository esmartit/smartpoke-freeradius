package com.esmartit.freeradiusservice.config

import com.esmartit.freeradiusservice.entities.NasEntity
import com.esmartit.freeradiusservice.entities.RadCheckEntity
import com.esmartit.freeradiusservice.entities.RadGroupCheckEntity
import com.esmartit.freeradiusservice.entities.RadGroupReplyEntity
import com.esmartit.freeradiusservice.entities.RadReplyEntity
import com.esmartit.freeradiusservice.entities.RadUserGroupEntity
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer

@Configuration
class DataRestConfig : RepositoryRestConfigurer {

    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
        config.exposeIdsFor(
            NasEntity::class.java, RadCheckEntity::class.java, RadGroupCheckEntity::class.java,
            RadGroupReplyEntity::class.java, RadReplyEntity::class.java, RadUserGroupEntity::class.java
        )
    }
}