package com.esmartit.freeradiusservice.entities

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "nas", schema = "public", catalog = "postgres")
data class NasEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int,
    @Column(name = "nasname", nullable = false)
    val name: String,
    @Column(name = "shortname", nullable = false)
    val shortName: String,
    @Column(name = "type", nullable = false)
    val type: String,
    @Column(name = "ports", nullable = true)
    val ports: Int,
    @Column(name = "secret", nullable = false)
    val secret: String,
    @Column(name = "server", nullable = true)
    val server: String,
    @Column(name = "community", nullable = true)
    val community: String,
    @Column(name = "description", nullable = true)
    val description: String
)

@RepositoryRestResource(collectionResourceRel = "nas", path = "nas")
interface NasRepository : CrudRepository<NasEntity, Int>