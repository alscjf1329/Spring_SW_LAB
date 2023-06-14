package com.sw.ks.Repository

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

    @Entity
    data class BoardEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        val writer :String? = null,
        val title: String,
        val content: String
    )

    @Repository
    interface BoardRepository: CrudRepository<BoardEntity, String>