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
@Table(name = "radreply")
data class RadReplyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int,
    @Column(name = "username", nullable = false)
    val username: String,
    @Column(name = "attribute", nullable = false)
    val attribute: String,
    @Column(name = "op", nullable = false)
    val op: String,
    @Column(name = "value", nullable = false)
    val value: String
)

@RepositoryRestResource(collectionResourceRel = "radreply", path = "radreply")
interface RadReplyRepository : CrudRepository<RadReplyEntity, Int>
