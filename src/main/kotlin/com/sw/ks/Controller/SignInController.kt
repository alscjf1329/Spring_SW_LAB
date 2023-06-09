package com.sw.ks.Controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@Controller
class SignInController {
    @PostMapping("/signup")
    @ResponseBody
    fun signUp(username: String, password: String, confirm_password: String, pin: String): Boolean {
        println("username = ${username}")
        println("password = ${password}")
        println("confirm_password = ${confirm_password}")
        println("pin = ${pin}")
        return true
    }
}
