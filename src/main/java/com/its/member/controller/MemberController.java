package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            return "memberPages/mypage";
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
    public String findById(@PathVariable("id") Long id,Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/detail";
    }

    @PostMapping("/ajax/{id}")
    public @ResponseBody MemberDTO findByIdAjax(@PathVariable("id") Long id){
        MemberDTO memberDTO = memberService.findById(id);
        return memberDTO;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        memberService.delete(id);
        return "redirect:/member/";
    }

    /**
     * /member/3 : 조회(get) R, 저장(post) C, 수정(put) U, 삭제(delete) D
     * ResponseEntity : data + code return
     */

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAjax(@PathVariable("id") Long id){
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK); // ajax 호출한 부분에 리턴으로 200 응답을 줌.
    }

    @GetMapping("/update")
    public String updateForm(HttpSession session,Model model){
        Long id = (Long) session.getAttribute("id");
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("updateMember",memberDTO);
        return "memberPages/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "redirect:/member/"+memberDTO.getId();
    }
    @PutMapping("/{id}")
    public ResponseEntity updateByAjax(@RequestBody MemberDTO memberDTO){
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/dup-check")
    public @ResponseBody String dupCheck(@RequestParam("memberEmail") String memberEmail){
        String checkResult = memberService.dupCheck(memberEmail);
        return checkResult;
    }
}
