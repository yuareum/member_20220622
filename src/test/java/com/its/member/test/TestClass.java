package com.its.member.test;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class TestClass {
    @Autowired
    private MemberService memberService;
    @Transactional
    @Rollback(value = true)
    @Test
    @DisplayName("save test")
    public void memberSaveTest(){
        MemberDTO memberDTO = new MemberDTO("testEmail","testPassword","testName",10,"010-1111-1111");
        Long saveId = memberService.save(memberDTO);
        MemberDTO findDTO = memberService.findById(saveId);
        assertThat(memberDTO.getMemberEmail()).isEqualTo(findDTO.getMemberEmail());
    }


}
