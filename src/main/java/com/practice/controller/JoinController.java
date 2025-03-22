package com.practice.controller;

import com.practice.dto.JoinDTO;
import com.practice.service.JoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


//@Controller
//@ResponseBody
@RestController // 위에 2개의 어노테이션 합친 것
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @GetMapping("/join")
    public String joinP() {

        return "join controller";
    }

//    @PostMapping("/join")
//    public String joinProcess(JoinDTO joinDTO) {

    @PostMapping("/join")
    public String joinProcess(@Valid JoinDTO joinDTO, BindingResult bindingResult) {
        // 유효성 검증 실패 시 오류 메시지 처리
        if (bindingResult.hasErrors()) {
            // 오류가 있을 경우, 오류 메시지를 반환하거나 적절히 처리
            System.out.println("valid 에러");
            return "redirect:/join?error";
        }

        joinService.joinProcess(joinDTO);
        return "redirect:/login";
    }
}
