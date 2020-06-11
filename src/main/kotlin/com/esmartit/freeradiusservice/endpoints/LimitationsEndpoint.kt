package com.esmartit.freeradiusservice.endpoints

import com.esmartit.freeradiusservice.entities.LimitationEntity
import com.esmartit.freeradiusservice.service.limitations.LimitationService
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api/limitations")
@EnableHypermediaSupport(type = [EnableHypermediaSupport.HypermediaType.HAL])
class LimitationsEndpoint(private val limitationsService: LimitationService) {

    @GetMapping(produces = ["application/hal+json"])
    fun getAll(): CollectionModel<LimitationEntity> {
        val link = linkTo(LimitationsEndpoint::class.java).withSelfRel()
        val content = limitationsService.getAll()
            .map { it.apply { generateLinks(name) } }
        return CollectionModel<LimitationEntity>(content, link)
    }

    @GetMapping(value = ["/{groupName}"], produces = ["application/hal+json"])
    fun getLimitationByName(@PathVariable("groupName") name: String): LimitationEntity {
        return limitationsService.getByName(name)?.apply { generateLinks(name) } ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Unable to find resource"
        )
    }

    @PostMapping(produces = ["application/hal+json"])
    fun createLimitation(@RequestBody limitation: LimitationEntity): LimitationEntity {
        return limitationsService.save(limitation).apply { generateLinks(name) }
    }

    @PatchMapping(produces = ["application/hal+json"])
    fun editLimitation(@RequestBody limitation: LimitationEntity): LimitationEntity {
        return limitationsService.save(limitation).apply { generateLinks(name) }
    }

    @DeleteMapping(value = ["/{groupName}"])
    fun deleteLimitation(@PathVariable("groupName") name: String) {
        limitationsService.delete(name)
    }

    private fun LimitationEntity.generateLinks(name: String) {
        val link = linkTo(LimitationsEndpoint::class.java).slash(name)
        add(link.withSelfRel())
        add(link.withRel("limitationEntity"))
    }
}
