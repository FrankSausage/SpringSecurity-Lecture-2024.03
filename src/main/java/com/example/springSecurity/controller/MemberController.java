package com.example.springSecurity.controller;

import com.example.springSecurity.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {

    @GetMapping("/detail/{mid}")
    public @ResponseBody String detail(@PathVariable int mid){
        Member member = new Member();
        log.info("detail");
        return member.toString();
    }

    @GetMapping("/insert")
    public @ResponseBody String insert(){
        Member m1 = new Member();
        m1.setName("james"); m1.setEmail("james@gamil.com");
        log.info(m1.toString());
        // Builder pattern
        Member m2 = Member.builder()
                .name("maria")
                .email("maria@naver.com")
                .build();
        log.info(m2.toString());
        return m1.toString() + "<br>" + m2.toString();
    }

    @GetMapping("/update")
    public @ResponseBody String update(){
        Member member = Member.builder()
                .mid(1).name("brian").email("brain@hanmail.net")
                .build();

        return member.toString();
    }
}
