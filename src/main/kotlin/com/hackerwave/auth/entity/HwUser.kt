package com.hackerwave.auth.entity

import com.hackerwave.auth.dto.HwUserDto
import java.util.*
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.Table

@Table(name = "hw_user")
class HwUser(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private var id : UUID?,

    @Column(name="email", unique = true, nullable = false)
    private val email : String,

    @Column(name="g_id", unique = true, nullable = false)
    private val gId: String

    ) {
    @PrePersist
    private fun setId(){
        if (id==null){
            this.id=UUID.randomUUID()
        }
    }

    fun toDto():HwUserDto{
        return HwUserDto(id, email, gId)
    }
}