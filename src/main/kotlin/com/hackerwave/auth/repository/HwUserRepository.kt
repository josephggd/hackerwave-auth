package com.hackerwave.auth.repository

import com.hackerwave.auth.entity.HwUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface HwUserRepository: JpaRepository<HwUser, UUID> {
    fun findBygIdAndEmail(gId:String, email: String): Optional<HwUser>
}