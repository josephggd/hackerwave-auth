package com.hackerwave.auth.dto

import com.hackerwave.auth.util.CommonStrings

class DateAggDto {
    private var date:String=""
    private var createAcctCount:Int=0
    private var deleteAcctCount:Int=0
    private var loginCount:Int=0
    private var createPostCount:Int=0
    private var deletePostCount:Int=0
    private var likePostCount:Int=0
    private var unlikePostCount:Int=0
    fun aggregate(actionDto: ActionDto):DateAggDto{
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