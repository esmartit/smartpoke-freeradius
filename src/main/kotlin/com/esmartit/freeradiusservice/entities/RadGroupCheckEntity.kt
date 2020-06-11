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
@Table(name = "radgroupcheck", schema = "public", catalog = "postgres")
data class RadGroupCheckEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int,
    @Column(name = "groupname", nullable = false)
    val groupName: String,
    @Column(name = "attribute", nullable = false)
    val attribute: String,
    @Column(name = "op", nullable = false)
    val op: String,
    @Column(name = "value", nullable = false)
    val value: String
)

@RepositoryRestResource(collectionResourceRel = "radgroupcheck", path = "radgroupcheck")
interface RadGroupCheckRepository : CrudRepository<RadGroupCheckEntity, Int> {
    fun findByGroupName(name: String): List<RadGroupCheckEntity>
    fun findByGroupNameAndAttribute(name: String, attribute: String): RadGroupCheckEntity?
    fun deleteByGroupNameAndAttribute(name: String, attribute: String)
    fun deleteByGroupName(name: String)
}
