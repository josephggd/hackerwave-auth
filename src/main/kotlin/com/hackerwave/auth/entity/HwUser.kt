package com.hackerwave.auth.entity

import com.hackerwave.auth.dto.UserDto
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "hw_user")
class HwUser(
    @Column(name="email", unique = true, nullable = false)
    private val email : String,

    @Column(name="g_id", unique = true, nullable = false)
    private val gId: String
    ) {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private var id : UUID? = null

    @PrePersist
    private fun setId(){
        if (this.id==null){
            this.id = UUID.randomUUID()
        }
    }

    fun toDto():UserDto{
        return UserDto(id, email, gId)
    }
}