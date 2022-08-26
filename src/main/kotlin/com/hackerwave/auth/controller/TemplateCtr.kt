package com.hackerwave.auth.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller("api/\${custom.api.prefix.version}/\${custom.api.prefix.view}/")
class TemplateCtr {

    @RequestMapping("/index.html")
    fun indexPage():String{
        return "index"
    }

}