package com.hackerwave.auth.dto

import com.hackerwave.auth.util.CommonStrings

class ActionAggDto {
    private var action:CommonStrings.UserAction?=null
    private var count:Int=0
    fun aggregate(actionDto: ActionDto):ActionAggDto{
        if (actionDto.action.toString()==this.action.toString()){
            this.count+=1
        }
        return this
    }
}