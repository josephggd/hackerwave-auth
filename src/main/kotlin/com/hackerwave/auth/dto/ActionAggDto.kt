package com.hackerwave.auth.dto

import com.hackerwave.auth.util.CommonStrings

class ActionAggDto {
    private lateinit var action:CommonStrings.UserAction
    private var count:Int=0
    fun aggregate(actionDto: ActionDto):ActionAggDto{
        this.action=actionDto.action
        this.count+=1
        return this
    }
}