package com.hackerwave.auth.dto

import com.hackerwave.auth.util.CommonStrings

data class DateAggDto(
    var date:String="",
    var createAcctCount:Int=0,
    var deleteAcctCount:Int=0,
    var loginCount:Int=0,
    var createPostCount:Int=0,
    var deletePostCount:Int=0,
    var likePostCount:Int=0,
    var unlikePostCount:Int=0
) {
    fun aggregate(actionDto: ActionDto):DateAggDto{
        this.date=actionDto.date
        when(actionDto.action.toString()){
            CommonStrings.UserAction.CREATE_ACCT.toString() -> createAcctCount+=1
            CommonStrings.UserAction.DELETE_ACCT.toString() -> deleteAcctCount+=1
            CommonStrings.UserAction.LOGIN.toString() -> loginCount+=1
            CommonStrings.UserAction.CREATE_POST.toString() -> createPostCount+=1
            CommonStrings.UserAction.DELETE_POST.toString() -> deletePostCount+=1
            CommonStrings.UserAction.LIKE_POST.toString() -> likePostCount+=1
            CommonStrings.UserAction.UNLIKE_POST.toString() -> unlikePostCount+=1
        }
        return this
    }
}