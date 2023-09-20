package com.sw.ks.Controller

import com.sw.ks.Repository.MemberEntity
import com.sw.ks.Repository.MemberRepository
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

val pin = "93190048" //이 인증번호를 아는 사람만 가입가능

@Controller
class SignInController(@Autowired val memberRepository: MemberRepository) {
    @PostMapping("/signup")
    @ResponseBody
    fun signUp(@RequestBody data: Map<String, String>): Boolean {
        val username = data["username"]
        val password = data["password"]
        val confirmPassword = data["confirmPassword"]
        val inputPin = data["pin"]



        val isExist = memberRepository.existsById(username?: "")
        if (isExist){ //이미 존재하는 아이디인지 확인
            println("isExist")
            return false
        }

        //모든 예외 통과 새로운 회원 생성. db에 삽입
        memberRepository.save(MemberEntity(username!!, password!!))
        println("Create New Member")
        return true
    }
}
