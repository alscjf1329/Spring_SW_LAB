package com.sw.ks.Repository

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Entity
data class MemberEntity(@Id val id: String, val password: String)

@Repository
interface MemberRepository: CrudRepository<MemberEntity, String>
