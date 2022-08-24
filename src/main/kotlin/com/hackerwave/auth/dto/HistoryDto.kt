package com.hackerwave.auth.dto

import com.hackerwave.auth.util.CommonStrings

class HistoryDto {
    private var action:CommonStrings.UserAction?=null
    private var count:Int=0
    fun aggregate(actionDto: ActionDto):HistoryDto{
        if (actionDto.action.toString()==this.action.toString()){
            this.count+=1
        }
        return this
    }
}