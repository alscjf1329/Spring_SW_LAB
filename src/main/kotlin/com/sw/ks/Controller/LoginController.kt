package com.sw.ks.Controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class LoginController {
    @GetMapping("/")
    fun getMain(): String {
        return "index"
    }

    @PostMapping("/")
    fun loginTry(id: String, password: String): String {
        println("$id $password")
        return "home"
    }
}
