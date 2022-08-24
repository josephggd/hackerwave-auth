package com.hackerwave.auth.dto

import com.hackerwave.auth.util.CommonStrings

data class ActionDto (val date: String, val time: String, val action:CommonStrings.UserAction)