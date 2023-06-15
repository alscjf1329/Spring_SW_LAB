package com.sw.ks.Controller

import com.sw.ks.Repository.BoardEntity
import com.sw.ks.Repository.BoardRepository
import com.sw.ks.Repository.MemberRepository
import jakarta.persistence.Id
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.w3c.dom.Text

val sessionMap = mutableMapOf<String, MutableMap<String, String>>()

fun tryEnteringSite(req: HttpServletRequest, destination: String): String {
    val cookies = req.cookies
    if (cookies != null) {
        val sk = cookies.filter { it.name == "sessionKey" }?.first()?.value

        if (sk.isNullOrBlank()) {
            return "redirect:/"
        }

        val loginState = sessionMap[sk]?.get("login")

        if (loginState.isNullOrBlank()) {
            return "redirect:/"
        }
        if (loginState != "true") {
            return "redirect:/"
        }
        return destination
    }
    return "redirect:/"
}

@Controller
class LoginController(@Autowired val memberRepository: MemberRepository) {
    @GetMapping("/")
    fun getLogin(req: HttpServletRequest): String {
        val cookies = req.cookies
        if (cookies != null) {
            val sk = cookies.filter { it.name == "sessionKey" }?.first()?.value

            if (sk.isNullOrBlank()) {
                return "index"
            }

            val loginState = sessionMap[sk]?.get("login")

            if (loginState.isNullOrBlank()) {
                return "index"
            }
            if (loginState != "true") {
                return "index"
            }
            return "redirect:/home"
        }
        return "index"
    }

    @PostMapping("/")
    fun loginTry(id: String, password: String, res: HttpServletResponse): String {
        val loginTryMember = memberRepository.findAllById(listOf(id))
        return if (loginTryMember.toList().isNotEmpty()) {
            if (password == loginTryMember.first().password) {
                sessionMap[id] = mutableMapOf(Pair("login", "true"))
                res.addCookie(Cookie("sessionKey", id))
                "redirect:/home"
            } else {
                "redirect:/"
            }
        } else {
            "redirect:/"
        }
    }

    @GetMapping("/logOut")
    fun logOut(@CookieValue("sessionKey") sk: String, res: HttpServletResponse): String {
        val cookie = Cookie("sessionKey", "")
        cookie.maxAge = 0
        res.addCookie(cookie)
        return "redirect:/ "
    }
}

@Controller
class HomeController() {
    @GetMapping("/home")
    fun getMain(req: HttpServletRequest, model: Model): String {
        val to = "home"
        val result = tryEnteringSite(req, to)
        if (to == result) {
            model.addAttribute("userName", req.cookies.filter { it.name == "sessionKey" }?.first()?.value)
        }
        return result
    }
}

@Controller
class BoardController(@Autowired val boardRepository: BoardRepository) {
    @GetMapping("/board")
    fun getBoard(req: HttpServletRequest, model: Model): String {
        model.addAttribute("boardList", boardRepository.findAll().toList())
        val to = "board"
        val result = tryEnteringSite(req, to)
        if (to == result) {
            model.addAttribute("userName", req.cookies.filter { it.name == "sessionKey" }?.first()?.value)
            model.addAttribute("boardList")
        }
        return result
    }

    @GetMapping("/addBoard")
    fun getAddBoard(req: HttpServletRequest, model: Model): String {
        val to = "addBoard"
        val result = tryEnteringSite(req, to)

        if (to == result) {
            val username = req.cookies.filter { it.name == "sessionKey" }?.first()?.value
            model.addAttribute("username", username)
            model.addAttribute("boardList", boardRepository.findAll().toList())
        }
        return result
    }

    @PostMapping("/addBoard")
    @ResponseBody
    fun createBoard(req: HttpServletRequest, @RequestBody data: Map<String, String>): Boolean {
        val to = "board"
        val result = tryEnteringSite(req, to)
        if (to == result) {
            val title = data["title"]!!
            val content = data["content"]!!
            val username = req.cookies.filter { it.name == "sessionKey" }?.first()?.value
            boardRepository.save(BoardEntity(null, username, title, content))
        }
        return true
    }

    @DeleteMapping("/deleteBoard")
    @ResponseBody
    fun deleteBoard(req: HttpServletRequest, @RequestBody data: Map<String, String>): Boolean {
        val to = "board"
        val result = tryEnteringSite(req, to)
        if (data["boardId"].isNullOrBlank()) return false
        if (data["writer"].isNullOrBlank()) return false
        if (to == result) {
            val username = req.cookies.filter { it.name == "sessionKey" }?.first()?.value
            if (data["writer"] == username) boardRepository.deleteById(data["boardId"]!!)
        }
        return true
    }
}

@Controller
class ContactController() {
    @GetMapping("/contact")
    fun getContact(req: HttpServletRequest, model: Model): String {
        val to = "contact"
        val result = tryEnteringSite(req, to)

        if (to == result) {
            model.addAttribute("userName", req.cookies.filter { it.name == "sessionKey" }?.first()?.value)
        }
        return result
    }
}

