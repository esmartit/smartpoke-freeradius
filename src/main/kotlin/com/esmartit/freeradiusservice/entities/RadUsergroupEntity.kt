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
@Table(name = "radusergroup")
data class RadUserGroupEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int,
    @Column(name = "username", nullable = false)
    val username: String,
    @Column(name = "groupname", nullable = false)
    val groupName: String,
    @Column(name = "priority", nullable = false)
    val priority: Int
)

@RepositoryRestResource(collectionResourceRel = "radusergroup", path = "radusergroup")
interface RadUserGroupRepository : CrudRepository<RadUserGroupEntity, Int> {
    fun findByUsername(username: String): RadUserGroupEntity?
}