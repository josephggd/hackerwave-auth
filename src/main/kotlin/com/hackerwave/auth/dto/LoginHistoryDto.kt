package com.hackerwave.auth.dto

import com.hackerwave.auth.util.CommonStrings
import java.util.*

class LoginHistoryDto{
    private var id:UUID? = null
    private var firstSeen:String? = null
    private var lastSeen:String? = null
    private var posts:Int=0
    private var logins:Int=0
    private var likes:Int=0
    private var unlikes:Int=0

    fun aggregate( loginDto: LoginDto ):LoginHistoryDto{
        if (loginDto.id!=null){
            this.id=loginDto.id
            if (this.firstSeen==null){
                this.firstSeen=loginDto.time
            }
            this.lastSeen=loginDto.time
            when (loginDto.action) {
                CommonStrings.UserAction.LOGIN.toString() -> {
                    this.logins+=1
                }
                CommonStrings.UserAction.POST.toString() -> {
                    this.posts+=1
                }
                CommonStrings.UserAction.LIKE.toString() -> {
                    this.likes+=1
                }
                CommonStrings.UserAction.UNLIKE.toString() -> {
                    this.unlikes+=1
                }
            }
        }
        return this
    }
}