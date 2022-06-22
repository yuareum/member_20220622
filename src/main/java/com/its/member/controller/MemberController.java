package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save-form")
    public String saveForm(){
        return "memberPages/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "memberPages/login";
    }

    @GetMapping("/login-form")
    public String loginForm(){
        return "memberPages/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            session.setAttribute("id", loginResult.getId());
            return "memberPages/main";
        }
        else {
            return "memberPages/login";
        }
    }

    @GetMapping("/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "memberPages/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id,Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/detail";
    }

    @GetMapping("/ajax/{id}")
    public @ResponseBody MemberDTO ajaxDetail(@PathVariable("id")Long id){
        MemberDTO memberDTO = memberService.findById(id);
        return memberDTO;
    }
}
