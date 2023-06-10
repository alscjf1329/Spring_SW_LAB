package com.sw.ks.Controller

import com.sw.ks.Repository.MemberRepository
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

val sessionMap = mutableMapOf<String, MutableMap<String, String>>()

@Controller
class LoginController(@Autowired val memberRepository: MemberRepository) {
    @GetMapping("/")
    fun getLogin(req: HttpServletRequest): String {
        val cookies = req.cookies
        if (cookies!=null){
            val sk = cookies.filter { it.name=="sessionKey" }.first().value

            val loginState = sessionMap[sk]?.get("login")

            if (loginState.isNullOrBlank()){
                return "index"
            }
            if (loginState!="true"){
                return "index"
            }
            return "redirect:/home"
        }
        return "index"
    }

    @PostMapping("/")
    fun loginTry(id: String, password: String, res: HttpServletResponse): String {
        val loginTryMember = memberRepository.findAllById(listOf(id))

        return if (loginTryMember.toList().isNotEmpty()){
            if (password==loginTryMember.first().password){
                sessionMap[id] = mutableMapOf(Pair("login", "true"))
                res.addCookie(Cookie("sessionKey", id))
                "redirect:/home"
            } else{
                "redirect:/"
            }
        } else{
            "redirect:/"
        }
    }
}

@Controller
class HomeController(){
    @GetMapping("/home")
    fun getMain(@CookieValue("sessionKey") sk: String): String {
        val loginState = sessionMap[sk]?.get("login")

        if (loginState.isNullOrBlank()){
            return "redirect:/"
        }
        if (loginState!="true"){
            return "redirect:/"
        }

        return "home"
    }
}
