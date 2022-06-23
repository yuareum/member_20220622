package com.its.member.test;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
// 자바 제공 test framework = assertj

@SpringBootTest
public class MemberTest {
    @Autowired
    private MemberService memberService;

    public MemberDTO newMember(int i){
        MemberDTO member = new MemberDTO("테스트용이메일"+i, "테스트용비밀번호"+i, "테스트용이름"+i,10+i,"테스트용전화번호"+i);
        return member;
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원가입 test")
    public void memberSaveTest(){
        Long saveId = memberService.save(newMember(1));
        MemberDTO findDTO = memberService.findById(saveId);
        assertThat(newMember(1).getMemberEmail()).isEqualTo(findDTO.getMemberEmail());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("로그인 test")
    public void loginTest(){
        final String memberEmail = "로그인용이메일";
        final String memberPassword = "로그인용비번";
        String memberName = "로그인용이름";
        int memberAge = 99;
        String memberPhone = "로그인용전화번호";
        MemberDTO memberDTO = new MemberDTO(memberEmail, memberPassword, memberName, memberAge, memberPhone);
        Long saveId = memberService.save(memberDTO);
        //로그인 객체 생성 후 로그인
        MemberDTO loginMemberDTO = new MemberDTO();
        loginMemberDTO.setMemberEmail(memberEmail);
        loginMemberDTO.setMemberPassword(memberPassword);
        MemberDTO loginResult = memberService.login(loginMemberDTO);
        // 로그인 결과가 not null 이면 테스트 통과
        assertThat(loginResult).isNotNull();

    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원 목록 test")
    public void memberListTest(){
        IntStream.rangeClosed(1,3).forEach(i ->{
            memberService.save(new MemberDTO("testEmail"+i,"testPassword"+i,"testName"+i,10+i,"testPhone"+i));
        });
        List<MemberDTO> memberDTOList = memberService.findAll();
        assertThat(memberDTOList.size()).isEqualTo(23);
    }

    @Test
    @DisplayName("회원 데이터 저장")
    public void memberSave(){
        IntStream.rangeClosed(1,20).forEach(i->{
            memberService.save(newMember(i));
        });
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원 삭제 test")
    public void memberDeleteTest(){
        /**
         * 신규 회원 등록
         * 삭제 처리
         * 해당 회원으로 조회시 null 이면 통과
         */
        Long saveId = memberService.save(newMember(999));
        memberService.delete(saveId);
        assertThat(memberService.findById(saveId)).isNull();
    }

}
